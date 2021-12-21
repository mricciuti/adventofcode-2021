package mri.advent.y2021

import java.lang.IllegalStateException
import java.util.*

/**
 * --- Day 18: Snailfish   ---
 */
class Day18(val data: String = "/day18.in") {



    // todo : use sealed class
    class SnailfishNumber(var left: SnailfishNumber? = null, var right: SnailfishNumber? = null, var value: Int? = null) {

        private fun isPair() = this.left != null && this.right != null
        private fun isRegularNumber() = this.value != null

        fun add(other: SnailfishNumber) = SnailfishNumber(left = this.clone(), right = other.clone())

        fun magnitude(): Int = if (isRegularNumber()) this.value!! else (3 * this.left!!.magnitude() + 2 * this.right!!.magnitude())

        /**  number reduction process (aka. headache process)  */
        fun reduce(): SnailfishNumber {
            // until no more reduction action available:  first try to explode pair, then try to split pair
            while (true) {
                if (tryExplodeOnePair())  continue
                if (trySplitOnePair())    continue
                break
            }
            return this
        }

        /** split pair (reduction process)  */
        fun split() {
            if (!isRegularNumber()) return
            this.left = SnailfishNumber(value = this.value!! / 2)
            this.right = SnailfishNumber(value = this.value!! / 2 + this.value!! % 2)
            this.value = null
        }

        private fun explode() {
            value = 0
            left = null
            right = null
        }

        /*  recursively find leftmost pair candidate for explosion in current pair number  */
        private fun getCandidatePairToExplode(depth: Int = 0): SnailfishNumber? {
            if (!this.isPair()) return null
            // test left part
            this.left!!.getCandidatePairToExplode(depth + 1)?.let { return it }
            // test current pair
            if (this.isPair() && depth >= 4 && this.left!!.isRegularNumber() && this.right!!.isRegularNumber()) {
                return this
            }
            // test right part
            this.right!!.getCandidatePairToExplode(depth + 1)?.let { return it }

            return null
        }

        /* recursively find leftmost pair to be split in current number*/
        private fun getCandidatePairToSplit(): SnailfishNumber? {
            if (this.isRegularNumber()) {
                if (this.value!! >= 10) return this
                return null
            }
            // test left side first, then right side
            this.left!!.getCandidatePairToSplit()?.let { return it }
            this.right!!.getCandidatePairToSplit()?.let { return it }

            return  null
        }

        private fun tryExplodeOnePair(): Boolean {
            getCandidatePairToExplode()?.let { pairToExplode ->
                debug(" found pair to explode  :  $pairToExplode")
                // flatten number tree ==> list of pairs
                val allPairs = flattenNumberPairs(this)

                // find index of the kamikaze pair in this list
                val expIndex = allPairs.indexOf(pairToExplode)
                if (expIndex < 0) throw IllegalStateException(" unexpected !")

                // propagate  to left, if possible
                if (expIndex > 1) {
                    for ( leftIndex in expIndex -2 downTo 0) {
                        if (allPairs[leftIndex].isRegularNumber()) {
                            trace("found left val to increase: ${allPairs[leftIndex]}")
                            allPairs[leftIndex].value = allPairs[leftIndex].value!!+ pairToExplode.left!!.value!!
                            break
                        }
                    }
                }
                // propagate  to right, if possible
                if (expIndex < allPairs.size - 1) {
                    for (rightIndex in expIndex + 2 until allPairs.size) {
                        if (allPairs[rightIndex].isRegularNumber()) {
                            trace("found right val to increase: ${allPairs[rightIndex]}")
                            allPairs[rightIndex].value = allPairs[rightIndex].value!!+ pairToExplode.right!!.value!!
                            break
                        }
                    }
                }
                pairToExplode.explode()
                return true
            }
            return false
        }

        private fun trySplitOnePair(): Boolean {
            getCandidatePairToSplit()?.let { candidate ->
                debug(" found pair to split :  $candidate")
                candidate.split()
                return true
            }
            return false
        }

        // recursively flatten given number tree into given pair numbers list - left to rigth
        private fun flattenNumberPairs(number: SnailfishNumber, pairs : MutableList<SnailfishNumber> = mutableListOf()): List<SnailfishNumber> {
            if (number.isRegularNumber()) {
                pairs.add(number)
            } else {
                flattenNumberPairs(number.left!!, pairs)
                pairs.add(number)
                flattenNumberPairs(number.right!!, pairs)
            }
            return pairs
        }

        override fun toString() = if (isRegularNumber())  "$value" else  "[$left,$right]"

        private fun clone(): SnailfishNumber = SnailfishNumber(this.left?.clone(), this.right?.clone(), this.value )

    }

    // sample:  [9,[8,7]]
    fun parseNumber(input: String): SnailfishNumber {
        val numbers = Stack<SnailfishNumber>()
        val result = SnailfishNumber()
        numbers.push(result)
        input.toCharArray().forEach { c ->
            val current = numbers.peek()
            if (c == '[') {
                current.left = SnailfishNumber()
                numbers.push(current.left)
            } else if (c.isDigit()) {
                current.value = c.digitToInt()
                numbers.pop()
            } else if (c == ',') {
                current.right = SnailfishNumber()
                numbers.push(current.right)
            } else if (c == ']') {
                numbers.pop()
            } else {
                throw IllegalArgumentException("unsupported char $c")
            }
        }
        return result
    }

    private fun parseInputData() = resourceAsStrings(data) .map { parseNumber(it) }

    fun part1(): Any {
        val numbers = parseInputData()
        var current = numbers[0]
        (1 until numbers.size).forEach { idx ->
            current = current.add(numbers[idx])
            current.reduce()
        }
        return current.magnitude()
    }

    fun part2(): Any {
        val numbers = parseInputData()
        val pairs = mutableListOf<Pair<SnailfishNumber, SnailfishNumber>>()
        // create all pairs
        for (first in numbers.indices) {
            for (second in numbers.indices) {
                  if (first != second ) pairs.add(Pair(numbers[first], numbers[second]))
            }
        }
        return pairs.map { pair ->
            val sum = pair.first.add(pair.second)
            sum.reduce()
            pair to sum.magnitude()
        }.maxOf { it.second }
    }
}

fun main() {
    setLogLevel(LogLevel.INFO)
    println(" part1:" + Day18().part1())
    println(" part2:" + Day18().part2())
}

