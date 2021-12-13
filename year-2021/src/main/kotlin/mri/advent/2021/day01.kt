package mri.advent.y2021

class Day01(val data: String = "/day01.in") {

    fun part2(): Any {
        val values = resourceAsStrings(data).map { it.toInt() }
        var prev = Integer.MAX_VALUE
        var counter = 0
        (0 until values.size - 2).forEach { idx ->
            val curr = values[idx] + values[idx + 1] + values[idx + 2]
            if (curr > prev) counter++
            prev = curr
        }
        return counter
    }

    fun part1(): Any {
        var prev = Integer.MAX_VALUE
        var counter = 0
        resourceAsStrings(data).map { it.toInt() }
            .forEach {
                if (it > prev) counter++
                prev = it
            }
        return counter
    }

}

fun main() {
    println(" part1:" + Day01().part1())
    println(" part2:" + Day01().part2())
}