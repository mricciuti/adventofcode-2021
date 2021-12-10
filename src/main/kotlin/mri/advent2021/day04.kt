package mri.advent2021

import java.util.*

class Day04 {

    data class Cell(val number: Int, var marked: Boolean = false) {
        fun mark(value: Int) {
            if (value == number) marked = true
        }
    }

    data class Board(var cells: Array<Array<Cell>>) {
        var completed: Boolean = false
        var score: Int = 0

        private fun allCells() = cells.flatMap { it.toList() }
        fun mark(number: Int) {
            allCells().forEach { it.mark(number) }
        }
        fun computeScore() = allCells().filter { !it.marked }.sumOf { it.number }
        fun completed(): Boolean {
            // ligne complete ?
            if (cells.any { it.none { !it.marked } }) return true
            // colonne complete ?
            if (columns().any { it.none { !it.marked } }) return true
            return false
        }
        private fun columns(): Array<Array<Cell>> {
            return Array(5) { colIdx -> Array(5) { rowId -> cells[rowId][colIdx] } }
        }
    }

    val boardSize = 5
    private fun parseBoard(input: Scanner): Board {
        return Board(Array(boardSize) { _ -> Array(boardSize) { _ -> Cell(input.nextInt()) } })
    }
    private fun parseBoards(input: Scanner): List<Board> {
        val boards = mutableListOf<Board>()
        while (input.hasNextLine()) {
            input.nextLine()
            boards.add(parseBoard(input))
        }
        return boards
    }

    fun d04Part1(): Any {
        scanner("/day04.in").use { input ->
            val numbers = input.nextLine().split(",").map { it.toInt() }
            val boards = parseBoards(input)
            for (num in numbers) {
                boards.forEach { it.mark(num) }
                boards.firstOrNull { it.completed() }?.let { return ("${it.computeScore() * num}") }
            }
        }
        throw IllegalStateException("solution not found")
    }

    fun d04Part2(): String {
        scanner("/day04.in").use { input ->
            val numbers = input.nextLine().split(",").map { it.toInt() }
            val boards = parseBoards(input)
            var lastScore = 0
            numbers.forEach { num ->
                val remaining = boards.filter { !it.completed }
                remaining.forEach { it.mark(num) }
                val winnings = remaining.filter { it.completed() }
                winnings.forEach { winning ->
                    winning.completed = true
                    winning.score = winning.computeScore() * num
                    lastScore = winning.score
                }
            }
            return "$lastScore"
        }
    }
}

fun main() {
    println(" part1:" + Day04().d04Part1())
    println(" part2:" + Day04().d04Part2())
}