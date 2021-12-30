package mri.advent.y2020

/**
 * --- Day 05:  Binary Boarding ---
 */
class Day05(val data: String = "/day05.in") {

    private fun reduceRange(initialRange: IntRange, flags: BooleanArray): Int {
        var range = initialRange
        repeat(flags.size) {idx ->
            range = if (flags[idx]) range.first until range.first + range.count() / 2 else range.first + range.count() / 2 .. range.last
        }
        return range.first
    }

    private fun seatId(seatString: String): Int {
        val row = reduceRange(0..127, BooleanArray(7) { seatString[it] == 'F' })
        val column = reduceRange(0..7, BooleanArray(3) {seatString[7 + it] == 'L'})
        return 8 * row + column
    }

    fun part1(): Any {
        return resourceAsStrings(data).map { seatId(it) }.maxOf { it }
    }
    fun part2(): Any {
        val seatIds = resourceAsStrings(data).map { seatId(it) }
        return (seatIds.minOf { it } + 1 until seatIds.maxOf { it }).first { !seatIds.contains(it) }
    }
}

fun main() {
    println(" part1:" + Day05().part1())
    println(" part2:" + Day05().part2())
}