package mri.advent2021

val DIRECTIONS = arrayListOf(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)

class Day09 {

    private val data = "/day09.in"
    private val sample = "/day09_sample.in"

    // Data model
    data class Cell(val Point: Point, var height: Int = 0, var bassin: Int = -1)
    data class Grid(val height: Int, val width: Int) {
        // internal cells
        val cells = Array(height) { y -> Array(width) { x -> Cell(Point(x, y)) } }

        fun cellAt(Point: Point) = cellAt(Point.x, Point.y)
        fun cellAt(x: Int, y: Int) = cells[y][x]
        fun allCells(): List<Cell> = cells.flatMap { it.toList() }
        fun neighbors(cell: Cell) = DIRECTIONS.map { cell.Point.to(it) }.filter { isValid(it) }.map { cellAt(it) }

        fun neighbors(pos: Point) = DIRECTIONS.map { pos.to(it) }.filter { isValid(it) }
        fun isValid(pos: Point) = pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height

        fun lowPoints(): List<Cell> {
            val res = mutableListOf<Cell>()
            cells.forEachIndexed { y, cols ->
                cols.forEachIndexed { x, _ ->
                    val cell = cellAt(x, y)
                    if (neighbors(cell.Point).count { cellAt(it).height <= cell.height } == 0) res.add(cell)
                }
            }
            return res
        }

        fun computeBassin(cell: Cell) {
            val neighbors = neighbors(cell).filter { it.height < 9 && it.bassin < 0 }
            neighbors.forEach { it.bassin = cell.bassin; computeBassin(it) }
        }
    }

    fun loadGrid(): Grid {
        val lines = resourceAsStrings(data)
        val grid = Grid(lines.size, lines[0].length)
        lines.forEachIndexed { y, line -> line.forEachIndexed { x, height -> grid.cellAt(x, y).height = height.toString().toInt() } }
        return grid
    }

    fun d09Part2(): Any {
        val grid = loadGrid()
        val lowpoints = grid.lowPoints()

        // propagation des lowpoints pour trouver les bassins associés
        var bassinId = 0
        lowpoints.forEach { lowPoint ->
            lowPoint.bassin = bassinId++
            grid.computeBassin(lowPoint)
        }
        // garder les 3 bassin les plus grands
        val cellsPerBassin = grid.allCells().groupBy { it.bassin }
            .filter { entry -> entry.key >= 0 }
            .map { it.value }
            .sortedByDescending { it.size }
            .take(3)
            .map { it.size }

        return cellsPerBassin[0] * cellsPerBassin[1] *  cellsPerBassin[2]
    }

    /**
     * The risk level of a low point is 1 plus its height
     * Find all of the low points on your heightmap.
     * What is the sum of the risk levels of all low points on your heightmap?
     */
    fun d09Part1(): Any {
        val grid = loadGrid()
        val lowpoints = grid.lowPoints()
        return lowpoints.map { it.height + 1 }.sum()
    }
}

fun main() {
    println(" part1:" + Day09().d09Part1())
    println(" part2:" + Day09().d09Part2())
}