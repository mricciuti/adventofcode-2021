package mri.advent

/**
 * --- Day 1: Report Repair ---
 */
class Day01(val data: String) {

    fun part2(): Any {
        val candidatePair =  resourceAsStrings(data).map { it.toLong() }
            .combinations(3)
            .first { it.sum() == 2020L }

        return candidatePair[0] * candidatePair[1] * candidatePair[2]
    }

    fun part1(): Any {
        val candidatePair =  resourceAsStrings(data).map { it.toLong() }
            .combinations(2)
            .first { it.sum() == 2020L }
        return candidatePair[0] * candidatePair[1]
    }
}

fun main() {
    val data = "/day01.in"
    println(" part1:" + Day01(data).part1())
    println(" part2:" + Day01(data).part2())
}
