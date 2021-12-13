package mri.advent.y2021

class Day02(val data: String = "/day02.in") {

    enum class CommandType { FORWARD, UP, DOWN }
    data class Command(val action: CommandType, val value: Int)

    private fun parseCommand(string: String): Command {
        val parts = string.split(" ")
        return Command(CommandType.valueOf(parts[0].uppercase()), parts[1].toInt())
    }

    data class Submarine(var x: Int = 0, var depth: Int = 0, var aim: Int = 0)

    fun part2(): Any {
        val submarine = Submarine()
        resourceAsStrings(data)
            .map { parseCommand(it) }
            .forEach {
                when (it.action) {
                    CommandType.FORWARD -> {
                        submarine.x += it.value; submarine.depth += submarine.aim * it.value
                    }
                    CommandType.DOWN -> submarine.aim += it.value
                    CommandType.UP -> submarine.aim -= it.value
                }
            }
        return submarine.x * submarine.depth
    }

    fun part1(): Any {
        val submarine = Submarine()
        resourceAsStrings(data)
            .map { parseCommand(it) }
            .forEach {
                /*
                    down X increases your aim by X units.
                    up X decreases your aim by X units.
                    forward X does two things:
                        It increases your horizontal position by X units.
                        It increases your depth by your aim multiplied by X.
                 */
                when (it.action) {
                    CommandType.FORWARD -> submarine.x += it.value
                    CommandType.DOWN -> submarine.depth += it.value
                    CommandType.UP -> submarine.depth -= it.value
                }
            }
        return submarine.x * submarine.depth
    }
}

fun main() {
    println(" part1:" + Day02().part1())
    println(" part2:" + Day02().part2())
}