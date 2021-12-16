package mri.advent.y2021

import java.util.*

/**
 * --- Day 15:  Chiton  ---
 */
class Day15(val data: String = "/day15.in") {

    data class Coord(val x: Int, val y: Int) {
        fun to(direction: Direction) = Coord(x + direction.x, y + direction.y)
    }

    data class Cell(val position: Coord, var riskLevel: Int = Int.MAX_VALUE)
    data class Grid(val cells: Array<Array<Cell>>) {
        val width = cells[0].size
        val height = cells.size
        val DIRECTIONS = arrayOf(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)

        // grid common features
        fun cellAt(coord: Coord) = cells[coord.y][coord.x]
        fun cellAt(x: Int, y: Int) = cells[y][x]
        fun neighbors(cell: Cell) = DIRECTIONS.map { cell.position.to(it) }.filter { inGrid(it) }.map { cellAt(it) }
        fun inGrid(pos: Coord) = pos.x in 0 until width && pos.y in 0 until height

        // Day 15 specific stuff
        fun incrementRiskLevel(currentLevel: Int, inc: Int) = (currentLevel + inc).let { if (it > 9) it - 9 else it }
        fun expand(factor: Int): Grid {
            val newCells = Array(height * factor) { y -> Array(width * factor) { x -> Cell(Coord(x, y)) } }
            (0 until height * factor).forEach { y ->
                (0 until width * factor).forEach { x ->
                    newCells[y][x].riskLevel = incrementRiskLevel(cellAt(x % width, y % height).riskLevel, x / width + y / height)
                }
            }
            return Grid(newCells)
        }

        override fun toString(): String {
            return cells.joinToString("\n") { it.joinToString("") { it.riskLevel.toString() } }
        }

    }

    data class Path(val position: Coord, val cost: Int) : Comparable<Path> {
        override fun compareTo(other: Path) = cost.compareTo(other.cost)
    }

    // Dijkstra matrix implementation.
    // find shortest path from source cell to dest cell in given grid
    fun dijkstra(grid: Grid, sourceCell: Cell, targetCell: Cell): Path {
        val targetPosition = grid.cellAt(grid.height - 1, grid.width - 1).position
        val pathsQueue = PriorityQueue<Path>()
        val visitedPositions = mutableSetOf<Coord>()
        pathsQueue.add(Path(Coord(0, 0), 0))
        while (pathsQueue.peek().position != targetPosition && pathsQueue.isNotEmpty()) {
            val head = pathsQueue.poll()
            grid.neighbors(grid.cellAt(head.position)).filter { cell -> !visitedPositions.contains(cell.position) }
                .map { cell -> Path(cell.position, head.cost + cell.riskLevel) }
                .forEach { path ->
                    visitedPositions.add(path.position)
                    pathsQueue.add(path)
                }
        }
        return pathsQueue.peek()
    }

    fun parseGrid() = resourceAsStrings(data).let { input ->
        Grid(Array(input.size) { y -> Array(input[y].length) { x -> Cell(Coord(x, y), input[y][x].toString().toInt()) } })
    }


    fun lowestRiskPathCost(grid: Grid): Int {
        val source = grid.cellAt(0, 0)
        val target = grid.cellAt(grid.width - 1, grid.height - 1)
        return dijkstra(grid, source, target).cost
    }

    fun part1() = lowestRiskPathCost(parseGrid())
    fun part2() = lowestRiskPathCost(parseGrid().expand(5))
}

fun main() {
    println(" part1: " + Day15().part1())
    println(" part2: " + Day15().part2())
}