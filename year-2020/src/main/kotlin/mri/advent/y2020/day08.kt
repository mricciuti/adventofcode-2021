package mri.advent.y2020

/**
 * --- Day 08:  Handheld Halting ---
 */
class Day08(val data: String = "/day08.in") {

    enum class OperationType { ACC, NOP, JMP }

    data class Instruction(val operation: OperationType, val argument: Int) {
        fun switch() = when (this.operation) {
            OperationType.JMP -> Instruction(OperationType.NOP, this.argument)
            OperationType.NOP -> Instruction(OperationType.JMP, this.argument)
            else -> throw IllegalArgumentException("acc instruction cannot switch")
        }
    }

    private fun parseInstructions() = resourceAsStrings(data).map { it.split(" ").let { Instruction(OperationType.valueOf(it[0].uppercase()), it[1].toInt()) } }

    private fun runInstructions(instructions: List<Instruction>, part2: Boolean = false): Int? {
        var accumulator = 0
        var next = 0
        val visited = mutableSetOf<Int>()
        while (true) {
            if (visited.contains(next)) {
                if (part2) return null else break
            }
            if (next > instructions.size) return null
            if (next == instructions.size) return accumulator
            visited.add(next)
            when (instructions[next].operation) {
                OperationType.NOP -> next++
                OperationType.ACC -> {
                    accumulator += instructions[next].argument; next++
                }
                OperationType.JMP -> next += instructions[next].argument
            }
        }
        return accumulator
    }

    fun part1() = runInstructions(parseInstructions()) ?: throw IllegalStateException("solution not found")

    private fun switchNoopJump(instructions: List<Instruction>, index: Int) = instructions.mapIndexed { idx, instruction ->
        if (index == idx) instruction.switch() else instruction
    }

    fun part2(): Any {
        val initInstructions = parseInstructions()
        val nops = initInstructions.mapIndexed { index, instruction -> index to instruction }.filter { it.second.operation == OperationType.NOP }
        val jpms = initInstructions.mapIndexed { index, instruction -> index to instruction }.filter { it.second.operation == OperationType.JMP }
        for (nop in nops) {
            runInstructions(switchNoopJump(initInstructions, nop.first), true)?.let { return it }
        }
        for (jmp in jpms) {
            runInstructions(switchNoopJump(initInstructions, jmp.first), true)?.let { return it }
        }
        throw java.lang.IllegalArgumentException("solution not found")
    }
}

fun main() {
    println(" part1:" + Day08().part1())
    println(" part2:" + Day08().part2())
}