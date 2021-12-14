package mri.advent.y2021

import kotlin.math.max
import kotlin.math.min

class Day05(val data: String = "/day05.in") {

    data class Line(val start: Point, val end: Point) {
        fun isHorizontal() = start.y == end.y
        fun isVertical() = start.x == end.x
    }

    data class Grid(val cells: Array<IntArray>) {
        constructor(size: Int): this(Array(size) { IntArray(size) { 0 } })

        fun allCells() = cells.flatMap { it.toList() }
        fun drawLine(line: Line) {
            if (line.isVertical()) {
                (min(line.start.y, line.end.y)..max(line.start.y, line.end.y)).forEach { y ->
                    cells[y][line.start.x]++
                }
            } else if (line.isHorizontal()) {
                (min(line.start.x, line.end.x)..max(line.start.x, line.end.x)).forEach { x ->
                    cells[line.start.y][x]++
                }
            } else { // diagonales
                val xRange = IntProgression.fromClosedRange(line.start.x, line.end.x, if (line.start.x < line.end.x) 1 else -1)
                var y = line.start.y
                xRange.forEach { x ->
                    cells[y][x]++
                    y += (if (line.start.y < line.end.y) 1 else -1)
                }
            }
        }
    }

    fun parseLine(line: String): Line {
        val parts = line.split(" ")
        return Line(Point.parse(parts[0]), Point.parse(parts[2]))
    }

    /**
     * Consider all of the lines. At how many points do at least two lines overlap?
     */
    fun part2(): Any {
        val lines = resourceAsStrings(data).map { parseLine(it) }
        val grid = Grid(1000)
        lines.forEach { grid.drawLine(it) }
        return grid.allCells().count { it >= 2 }
    }

    /**
     * Consider only horizontal and vertical lines. At how many points do at least two lines overlap?
     */
    fun part1(): Any {
        val lines = resourceAsStrings(data).map { parseLine(it) }.filter { it.isVertical() || it.isHorizontal() }
        val grid = Grid(1000)
        lines.forEach { grid.drawLine(it) }
        return grid.allCells().count { it >= 2 }
    }
}

fun main() {
    println(" part1:" + Day05().part1())
    println(" part2:" + Day05().part2())
}