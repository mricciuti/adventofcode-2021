package mri.advent.y2021

import kotlin.math.abs

/**
 *  --- Day 7: The Treachery of Whales ---
 */
class Day07(val data: String = "/day07.in") {

    /**
     * brute force
     * @param cost : cout de déplacement d'un crab
     */
    fun exec(cost: (position: Int) -> Int): Any {
        val crabs = resourceAsStrings(data)[0].split(",").map { it.toInt() }.toTypedArray()
        val min = crabs.minByOrNull { it } ?: throw IllegalStateException("no crabs")
        val max = crabs.maxByOrNull { it } ?: throw IllegalStateException("no crabs")
        var bestScore = Int.MAX_VALUE
        (min..max).forEach { p ->
            val currentCost = crabs.map { cost(abs(it - p)) }.sum()
            if (currentCost < bestScore) {
                bestScore = currentCost
            }
        }
        return "$bestScore"
    }
}

fun main() {
    println(" part1:" + Day07().exec { pos -> pos })
    println(" part2:" + Day07().exec { pos -> (1..pos).sumOf { it } })
}