package mri.advent.y2021

/**
 * --- Day 14: Extended Polymerization ---
 */
class Day14(val data: String = "/day14.in") {

    // Pair insertions rules ----------------------
    data class PairInsertionRule(val pair: String, val charToInsert: Char)
    private fun parsePairInsertionRule(line: String): PairInsertionRule {
        line.split(" -> ").let { return PairInsertionRule(it[0], it[1][0]) }
    }
    private val pairInsertionRules = mutableMapOf<String, PairInsertionRule>()

    // Map pairs ==> nbOccurences
    private val pairCountersMap = mutableMapOf<String, Long>()
    private fun incrementPairCounter(pair: String, diff: Long = 1) {
        val currentVal = pairCountersMap.computeIfAbsent(pair) { 0 }
        pairCountersMap[pair] = currentVal + diff
    }

    // Map letters ==> nbOccurences
    private val letterCountersMap = mutableMapOf<Char, Long>()
    private fun incrementLetterCounter(letter: Char, diff: Long = 1) {
        letterCountersMap[letter] = diff + letterCountersMap.computeIfAbsent(letter) { 0 }
    }

    private fun processStep() {
        val mapCopy = mutableMapOf(*pairCountersMap.map { (k,v) -> k to v }.toTypedArray())
        // process each pairs
        mapCopy.forEach { (pair, count) ->
            pairInsertionRules[pair]?.let { rule ->
                val newPairLeft = "${pair[0]}${rule.charToInsert}"
                val newPairRight = "${rule.charToInsert}${pair[1]}"
                // increase occurences of new generated left/right pairs
                incrementPairCounter(newPairLeft, count)
                incrementPairCounter(newPairRight, count)
                // current pair splitted => decrease occurences
                incrementPairCounter(pair, -count)
                // update letters occcurences counters
                incrementLetterCounter(rule.charToInsert, count)
            }
        }
    }

    private fun process(nbSteps: Int): Long {
        // INIT - Load input data, init maps --------------
        var polymerTemplate = ""
        resourceAsStrings(data).forEach { line ->
            if (line.contains("->")) parsePairInsertionRule(line).let { pairInsertionRules.put(it.pair, it) }
            else if (line.isNotEmpty()) polymerTemplate = line
        }
        (0 until polymerTemplate.length - 1).forEach { incrementPairCounter(polymerTemplate.substring(it, it + 2)) }
        polymerTemplate.forEach { incrementLetterCounter(it) }

        // Processing polymerization steps -----------------
        repeat(nbSteps) { processStep() }

        // compute result -----------------------------------
        // What do you get if you take the quantity of the most common element and subtract the quantity of the least common element?
        return letterCountersMap.maxByOrNull { it.value }!!.value  - letterCountersMap.minByOrNull { it.value }!!.value
    }

    fun part1() = process(10)
    fun part2() = process(40)
}

fun main() {
    println(" part1: " + Day14().part1())
    println(" part2: " + Day14().part2())
}