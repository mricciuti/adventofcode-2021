package mri.advent.y2021

/**
 * --- Day 13: Transparent Origami ---
 */
class Day13(val data: String = "/day13.in") {

    enum class FoldDirection { UP, LEFT }
    data class FoldInstruction(val direction: FoldDirection, val value: Int)
    data class Grid(val width: Int, val height: Int) {

        val cells = Array(height) { Array(width) { false } }
        fun allCells(): List<Boolean> = cells.flatMap { it.toList() }

        fun fold(fold: FoldInstruction): Grid {
            val foldUp = fold.direction == FoldDirection.UP
            // create new grid , folded horizontally or vertically
            val foldedGrid = if (foldUp) Grid(this.width, fold.value) else Grid(fold.value, this.height)
            // fill new grid
            (0 until foldedGrid.width).forEach { x ->
                (0 until foldedGrid.height).forEach { y ->
                    foldedGrid.cells[y][x] = this.cells[y][x]
                            || (if (foldUp) this.cells[this.height - y - 1][x] else this.cells[y][this.width - x - 1])
                }
            }
            return foldedGrid
        }

        override fun toString() = cells.joinToString("\n") { it.joinToString("") { if (it) "#" else " " } }
    }

    // parsing : "fold along y=13"  => extract  "y" et "13"
    // https://regex101.com/ is your friend...
    fun parseFolding(str: String): FoldInstruction {
        "(\\w+) (\\w+) ([xy])(=)(\\d+)".toRegex().find(str)
            ?.let {
                return FoldInstruction(if (it.groupValues[3] == "x") FoldDirection.LEFT else FoldDirection.UP, it.groupValues[5].toInt())
            } ?: throw IllegalArgumentException("invalid line: $str")
    }

    fun process(firstFoldOnly: Boolean = false): Any {
        // Parse input data
        val points = mutableListOf<Point>()
        val foldings = mutableListOf<FoldInstruction>()
        resourceAsStrings(data).forEach { str ->
            if (str.contains(",")) points.add(Point.parse(str))
            else if (str.contains("=")) foldings.add(parseFolding(str))
        }
        // ! (no said in puzzle description) folding is always half-size
        val gridWidth = foldings.first { it.direction == FoldDirection.LEFT }.value * 2  + 1
        val gridHeight = foldings.first { it.direction == FoldDirection.UP }.value * 2 + 1
        var grid = Grid(gridWidth, gridHeight)
        points.forEach { grid.cells[it.y][it.x] = true }

        // folding process
        if (firstFoldOnly) foldings.first().let { grid = grid.fold(it) }
        else foldings.forEach { grid = grid.fold(it) }

        // results
        println(grid.toString())
        return grid.allCells().count { it }
    }

    fun part1(): Any {
        return process(true)
    }

    fun part2(): Any {
        return process(false)
    }
}

fun main() {
    println(" part1:" + Day13().part1())
    println(" part2:" + Day13().part2())
}