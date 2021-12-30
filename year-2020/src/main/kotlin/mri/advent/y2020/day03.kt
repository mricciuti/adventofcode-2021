package mri.advent.y2020

/**
 * - Day 3: Toboggan Trajectory ---
 */
class Day03(val data: String = "/day03.in") {

    data class Coord(val x: Int, val y: Int) {
        fun plus(other: Coord) = Coord(x + other.x, y + other.y)
    }
    data class Map(val cell: Array<CharArray>) {
        fun cellAt(coord: Coord) = cell[coord.y][coord.x % cell[0].size]
    }

    private fun nbTreesForSlop(map: Map, slop: Coord): Long {
        var nbTrees = 0L
        var current = Coord(0, 0)
        while (current.y < map.cell.size) {
            if (map.cellAt(current) == '#') nbTrees ++
            current = current.plus(slop)
        }
        return nbTrees
    }

    fun part1(): Any {
        val map = parseInput()
        return nbTreesForSlop(map, Coord(3, 1))
    }

    fun part2(): Any {
        val map = parseInput()
        return listOf( 1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
            .map { Coord(it.first, it.second) }
            .map { nbTreesForSlop(map, it) }
            .reduce { acc, i -> acc * i }
    }
    private fun parseInput(): Map {
        val lines = resourceAsStrings(data)
        return Map(Array(lines.size) { lines[it].toCharArray() })
    }
}

fun main() {
    println(" part1:" + Day03().part1())
    println(" part2:" + Day03().part2())
}