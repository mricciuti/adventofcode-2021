package mri.advent

/**
 * Day 11: Dumbo Octopus
 */
class Day11(val data: String) {

    // Data model
    data class Cell(val point: Point, var energy: Int = 0, var flashed: Boolean = false)
    data class Grid(val height: Int, val width: Int) {
        val cells = Array(height) { y -> Array(width) { x -> Cell(Point(x, y)) } }
        var currentStep = 0
        var nbFlashes = 0
        var firstAllFlashStep = -1
        val DIRECTIONS = arrayOf(
            Direction.UP, Direction.UPRIGHT, Direction.RIGHT, Direction.DOWNRIGHT,
            Direction.DOWN, Direction.DOWNLEFT, Direction.LEFT, Direction.UPLEFT
        )

        fun cellAt(Point: Point) = cellAt(Point.x, Point.y)
        fun cellAt(x: Int, y: Int) = cells[y][x]
        fun allCells(): List<Cell> = cells.flatMap { it.toList() }
        fun neighbors(cell: Cell) = DIRECTIONS.map { cell.point.to(it) }.filter { inGrid(it) }.map { cellAt(it) }
        fun inGrid(pos: Point) = pos.x in 0 until width && pos.y in 0 until height

        fun step() {
            currentStep++

            // init phase - increment energy & reset flash flag
            allCells().forEach { it.flashed = false; it.energy++ }

            // flash phase
            var flashingCandidates = allCells().filter { it.energy > 9 && !it.flashed }
            while (flashingCandidates.isNotEmpty()) {
                flashingCandidates.forEach { flash(it) }
                flashingCandidates = allCells().filter { it.energy > 9 && !it.flashed }
            }
            // test if all flashed
            if (firstAllFlashStep < 0 && allCells().none { !it.flashed }) {
                firstAllFlashStep = currentStep
            }
            // reset energy &increment flashing cells counter
            allCells().filter { it.flashed }.forEach { it.energy = 0; nbFlashes++ }
        }

        fun flash(cell: Cell) {
            cell.flashed = true
            neighbors(cell).forEach { it.energy++ }
        }
    }

    fun loadGrid(): Grid {
        val lines = resourceAsStrings(data)
        val grid = Grid(lines.size, lines[0].length)
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, energy -> grid.cellAt(x, y).energy = energy.toString().toInt() }
        }
        return grid
    }

    fun part1(): Int {
        val grid = loadGrid()
        repeat(100) { grid.step() }
        return grid.nbFlashes
    }

    fun part2(): Int {
        val grid = loadGrid()
        var res = grid.firstAllFlashStep
        while (res < 0) {
            grid.step()
            res = grid.firstAllFlashStep
        }
        return res
    }
}

fun main() {
    val dataFile = "/day11.in"
    println(" part1:" + Day11(dataFile).part1())
    println(" part2:" + Day11(dataFile).part2())
}