package mri.advent.y2021

import java.net.URL
import java.util.*

//  Basic Logging =======================================
enum class LogLevel { INFO, DEBUG, TRACE }
var LOG_LEVEL = LogLevel.INFO

fun debug(str: String, padding: Int = 0) {
    if (LogLevel.DEBUG <= LOG_LEVEL) log(str, padding)
}
fun trace(str: String, padding: Int = 0) {
    if (LogLevel.TRACE <= LOG_LEVEL) log(str, padding)
}
fun info(str: String, padding: Int = 0) {
    log(str, padding)
}
fun log(str: String, padding: Int = 0) {
    println("${"".padStart(padding, ' ')}$str")
}

class Utils

fun resourceAsInts(resource: String) = resourceAsStrings(resource).map { it.toInt() }

fun resourceAsStrings(resource: String): List<String> {
    Utils::class.java.getResource(resource)
        ?.let { return resourceAsStrings(it) }
        ?: throw IllegalArgumentException("Resource not found: $resource")
}

fun resourceAsStrings(url: URL): List<String> {

    with(Scanner(url.readText())) {
        val lines = mutableListOf<String>()
        while (this.hasNextLine()) lines.add(this.nextLine())
        return lines
    }
}

fun scanner(resource: String): Scanner {
    Utils::class.java.getResource(resource)
        ?.let { return Scanner(it.readText()) }
        ?: throw IllegalArgumentException("Resource not found: $resource")
}