package mri.advent2021

import java.net.URL
import java.util.*

class Utils

fun resourceAsStrings(resource: String): List<String> {
    Utils::class.java.getResource(resource)
        ?.let { return resourceAsStrings(it) }
        ?: throw IllegalArgumentException("Resource not found: $resource")
}

fun resourceAsStrings(url: URL): List<String> {
    with (Scanner(url.readText())) {
        val lines = mutableListOf<String>()
        while(this.hasNextLine()) lines.add(this.nextLine())
        return lines
    }
}

fun scanner(resource: String): Scanner {
    Utils::class.java.getResource(resource)
        ?.let { return Scanner(it.readText()) }
        ?: throw IllegalArgumentException("Resource not found: $resource")
}