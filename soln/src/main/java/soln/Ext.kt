package soln

import java.net.URLEncoder

fun ByteArray.allUrlEncoded(): String = buildString {
    val table = "0123456789ABCDEF"
    for (byte in this@allUrlEncoded) {
        append('%')
        val i = byte.toInt() and 0xff
        append(table[i ushr 4])
        append(table[i and 0x0f])
    }
}

fun gopherUrl(host: String, port: Int, data: ByteArray): String = "gopher://$host:$port/_${data.allUrlEncoded()}"

fun redirectUrl(url: String): String = "https://hb.ceclin.top/redirect-to?url=${URLEncoder.encode(url, "UTF-8")}"
