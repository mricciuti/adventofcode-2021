package mri.advent

private fun d01Part2(): String {
    val values = resourceAsStrings("/src/main/resources/day01.in").map { it.toInt() }
    var prev = Integer.MAX_VALUE
    var counter = 0
    (0 until values.size - 2).forEach { idx ->
        val curr = values[idx] + values[idx + 1] + values[idx + 2]
        if (curr > prev) counter++
        prev = curr
    }
    return "$counter"
}

private fun d01Part1(): String {
    var prev = Integer.MAX_VALUE
    var counter = 0
    resourceAsStrings("/src/main/resources/day01.in").map { it.toInt() }
        .forEach {
            if (it > prev) counter++
            prev = it
        }
    return "$counter"
}

fun main() {
    println(" part1:" + d01Part1())
    println(" part2:" + d01Part2())
}