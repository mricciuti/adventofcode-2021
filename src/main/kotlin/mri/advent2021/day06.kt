package mri.advent2021

/**
 * Lanternfish
 */
class Day06 {
    private val data = "/day06.in"
    private val sample = "/day06_sample.in"

    // buffer pour stocker la population
    private val bufferSize = 1_000_000_000
    private val buffer = Array(bufferSize) { -1 }
    private var bufferIndex = 0
    private fun resetBuffer() {
        (0 until bufferIndex).forEach { buffer[it] = -1 }
        bufferIndex = 0
    }

    // pour 1 poisson => population générée au bout de x jours
    private fun fishGrowth(fishAge: Int, nbDays: Int): LongArray { //Long {
        resetBuffer()
        buffer[bufferIndex++] = fishAge
        (1..nbDays).forEach { day ->
            (0 until bufferIndex).forEach { idx ->
                // Each day, a 0 becomes a 6 and adds a new 8 to the end of the list,
                // while each other number decreases by 1 if it was present at the start of the day.
                if (buffer[idx] > 0) {
                    buffer[idx]--
                } else {
                    buffer[idx] = 6  // reset cycle
                    buffer[bufferIndex++] = 8
                }
            }
        }
        val populationPerAge = LongArray(9) { 0L }
        (0 until bufferIndex).forEach { populationPerAge[buffer[it]]++ }
        return populationPerAge
    }

    fun simulate(nbDays: Int): Any {
        val fishes = resourceAsStrings(data)[0].split(",").map { it.toInt() }.toTypedArray()
        val growthPerAge = mutableMapOf<Int, LongArray>()
        val fishesPerAge = LongArray(9) { 0L }

        // step 1 : half-life
        (fishes.indices).forEach { fIdx ->
            if (growthPerAge.get(fishes[fIdx]) == null) {
                growthPerAge.put(fishes[fIdx], fishGrowth(fishes[fIdx], nbDays / 2))
            }
            growthPerAge.get(fishes[fIdx])!!.forEachIndexed { index, nb -> fishesPerAge[index] += nb }
        }
        // step 2 : second half life
        val fishesPerAge2 = Array(9) { 0L }
        fishesPerAge.forEachIndexed { age, nb ->
            if (growthPerAge[age] == null) {
                growthPerAge[age] = fishGrowth(age, nbDays / 2)
            }
            growthPerAge[age]!!.forEachIndexed { idx, iNb -> fishesPerAge2[idx] += iNb * nb }
        }
        return fishesPerAge2.sum()
    }
}

fun main() {
    println(" part1:" + Day06().simulate(80))
    println(" part2:" + Day06().simulate(256))
}