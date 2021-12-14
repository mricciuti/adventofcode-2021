package mri.advent.y2020

import java.net.URL
import java.util.*

class Utils

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

fun <T : Any> List<T>.combinations(r: Int): Sequence<List<T>> {
    val n = count()
    if (r > n) return sequenceOf()
    return sequence {
        val indices = (0 until r).toMutableList()
        while (true) {
            yield(indices.map { this@combinations[it] })
            var i = r - 1
            loop@ while (i >= 0) {
                if (indices[i] != i + n - r) break@loop
                i--
            }
            if (i < 0) break
            indices[i] += 1
            (i + 1 until r).forEach { indices[it] = indices[it - 1] + 1 }
        }
    }
}
