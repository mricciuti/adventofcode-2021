package mri.advent.y2021

/**
 * --- Day 25:  ---
 */
val EMPTY = '.'
val SOUTH = 'v'
val EAST = '>'

class Day25(val data: String = "/day25.in") {



    data class Cell(val x: Int, val y: Int, var type: Char) {

    }
    data class Grid(val cells: Array<Array<Cell>>) {

        fun neighbor(cell: Cell, direction: Direction ): Cell {
            if (direction == Direction.RIGHT) {
                return if (cell.x == cells[0].size - 1) cells[cell.y][0] else  cells[cell.y][cell.x + 1]
            } else if (direction == Direction.DOWN) {
                return if (cell.y == cells.size - 1) cells[0][cell.x] else  cells[cell.y + 1][cell.x]
            } else
                throw IllegalArgumentException("invalid direction")
        }
        fun allCells() = cells.flatMap { it.toList() }
        fun move(): Boolean {
            // try moving east-facing
            val movesEast = allCells().filter { it.type == EAST }.map { it to neighbor(it, Direction.RIGHT)}
                .filter { it.second.type == EMPTY}
            movesEast.forEach { it.first.type = EMPTY; it.second.type = EAST }

            // try moving south-facing
            val movesSouth = allCells().filter { it.type == SOUTH }.map { it to neighbor(it, Direction.DOWN)}
                .filter { it.second.type == EMPTY}
            movesSouth.forEach { it.first.type = EMPTY; it.second.type = SOUTH }

            return movesEast.size + movesSouth.size > 0
        }

        override fun toString() = cells.joinToString("\n") { it.joinToString("") { it.type.toString()} }
    }

    fun process(): Any {
        val lines = resourceAsStrings(data)
        val grid = Grid(Array(lines.size) { y -> Array(lines[0].length) { x -> Cell(x, y ,lines[y][x])} })

        var step = 1
        while (true) {
            if(!grid.move()) break
            step ++
            debug(grid.toString())
            debug("")
        }


        return step
    }

    fun part2(): Any {
        return process()
    }

    fun part1(): Any {
        return process()
    }
}

fun main() {
    println(" part1:" + Day25().part1())
 //   println(" part2:" + Day25().part2())
}