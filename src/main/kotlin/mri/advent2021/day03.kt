package mri.advent2021

import kotlin.math.pow

fun main() {
    println(" part1:" + d03Part1())
    println(" part2:" + d03Part2())
}

private fun d03Part2(): Any {
    val lines = resourceAsStrings("/day03.in")
    val matrix = Array(lines.size) { idx -> lines[idx].toCharArray().map { if (it == '0') 0 else 1 }.toIntArray() }

    // oxygen
    var i = 0
    var oxyNumbers = mutableListOf<IntArray>()
    matrix.forEach { oxyNumbers.add(it) }
    while (oxyNumbers.size > 1) {
        oxyNumbers = reduce(i++, oxyNumbers)
    }

    // CO2
    var co2Numbers = mutableListOf<IntArray>()
    matrix.forEach { co2Numbers.add(it) }
    i = 0
    while (co2Numbers.size > 1) {
        co2Numbers = reduce(i++, co2Numbers, true)
    }

    return hexToDecimal(oxyNumbers[0]) * hexToDecimal(co2Numbers[0])
}

private fun d03Part1(): Any {
    val numbers = resourceAsStrings("/day03.in")
    val digitLength = numbers[0].length
    val counters = Array(digitLength) { 0 }
    numbers.forEach {
        it.toCharArray().forEachIndexed { index, i ->
            counters[index] += if (i == '0') 0 else 1
        }
    }
    val gamma = IntArray(digitLength) { idx -> if (counters[idx] > numbers.size / 2) 1 else 0 }
    val epsilon = IntArray(digitLength) { idx -> if (counters[idx] < numbers.size / 2) 1 else 0 }
    return hexToDecimal(gamma) * hexToDecimal(epsilon)
}

fun reduce(index: Int, source: List<IntArray>, co2: Boolean = false): MutableList<IntArray> {
    val ones = source.count { it[index] == 1 }
    val zeros = source.count { it[index] == 0 }
    val mostCommon = if (ones >= zeros) 1 else 0
    val lessCommon = if (zeros <= ones) 0 else 1
    return source.filter { it[index] == (if (co2) lessCommon else mostCommon) }.toMutableList()
}

//  copy/paste web
fun hexToDecimal(ints: IntArray): Int {
    val hexValue = ints.joinToString("") { it.toString() }.toLong()
    var num = hexValue
    var decimalNumber = 0
    var i = 0
    var remainder: Long
    while (num != 0L) {
        remainder = num % 10
        num /= 10
        decimalNumber += (remainder * 2.0.pow(i.toDouble())).toInt()
        ++i
    }
    return decimalNumber
}
