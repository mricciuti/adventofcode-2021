package mri.advent.y2021

import kotlin.math.max

/**
 * --- Day 17:  ---
 */
class Day17(val data: String = "/day17.in") {

    data class Coord(var x: Int, var y: Int)
    data class Range(val upLeft: Coord, val bottomRight: Coord) {
        fun contains(position: Coord) = (position.x in upLeft.x..bottomRight.x) && (position.y in bottomRight.y..upLeft.y)
    }
    data class Probe(val velocity: Coord) {
        private val position: Coord = Coord(0, 0)
        var maxY = position.y

        fun simulate(maxSteps: Int, targetRange: Range): Boolean {
            for (step in 0 until maxSteps) {
                // update position
                position.x += velocity.x; position.y += velocity.y
                // update velocity
                velocity.x += if (velocity.x > 0) -1 else (if (velocity.x < 0) 1 else 0); velocity.y -= 1
                maxY = max(maxY, position.y)
                if (targetRange.contains(position))  return true
                if (position.x > targetRange.bottomRight.x || position.y < targetRange.bottomRight.y)  break
            }
            return false
        }
    }

    fun parseTargetRange(input: String): Range {
        return "target area: x=(-?\\d+)..(-?\\d+), y=(-?\\d+)..(-?\\d+)".toRegex().find(input)
            ?.let {
                return Range(Coord(it.groupValues[1].toInt(), it.groupValues[4].toInt()),  Coord(it.groupValues[2].toInt(), it.groupValues[3].toInt()))
            } ?: throw IllegalArgumentException("invalid line: $input")
    }

    fun getValidProbePaths(): List<Probe> {
        val MAX_STEPS = 1000
        val FACTOR = 2
        val targetRange = parseTargetRange(resourceAsStrings(data)[0])
        val candidateVelocities = mutableListOf<Coord>()
        (0 until targetRange.bottomRight.x * FACTOR).forEach { x ->
            (targetRange.bottomRight.y * FACTOR until -(targetRange.bottomRight.y * FACTOR)).forEach { y ->
                candidateVelocities.add(Coord(x, y))
            }
        }
        return candidateVelocities.map { Probe(it) }.filter { it.simulate(MAX_STEPS, targetRange) }
    }

    fun part1() = getValidProbePaths().maxOf { it.maxY }
    fun part2() = getValidProbePaths().count()
}

fun main() {
    println(" part1:" + Day17().part1())
    println(" part2:" + Day17().part2())
}