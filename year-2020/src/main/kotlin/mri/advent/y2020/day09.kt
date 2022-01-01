package mri.advent.y2020

/**
 * --- Day 09:  ---
 */
class Day09(val data: String = "/day09.in") {

    private fun sumExists(numbers: List<Long>, expected: Long): Boolean {
        for (i in numbers.indices) {
            for (j in numbers.indices) {
                if (i != j && (numbers[i] + numbers[j] == expected))
                    return true
            }
        }
        return false
    }

    fun part1(preambleSize: Int = 25): Long {
        val numbers = resourceAsStrings(data).map { it.toLong() }
        (preambleSize until numbers.size).forEach { idx ->
            info("testing number #$idx ${numbers[idx]}")
            if (!sumExists(numbers.subList(idx - preambleSize, idx), numbers[idx])) return numbers[idx]
        }
        return -1L
    }

    fun part2(preambleSize: Int = 25): Any {
        val invalidNumber = part1(preambleSize)
        val numbers = resourceAsStrings(data).map { it.toLong() }
        for (num in numbers.indices) {
            var sum = 0L
            var idx = num
            while (sum < invalidNumber && idx < numbers.size) {
                sum += numbers[idx++]
            }
            if (sum == invalidNumber) {
                numbers.subList(num, idx).let { return it.minOf { it } + it.maxOf { it } }
            }
        }
        throw IllegalStateException("solution not found")
    }

}

fun main() {
    println(" part1:" + Day09().part1())
    println(" part2:" + Day09().part2())
}