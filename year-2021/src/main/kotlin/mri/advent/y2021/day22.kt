package mri.advent.y2021

import kotlin.math.max
import kotlin.math.min
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

// qq extensions pratiques sur les IntRange
fun IntRange.overlaps(other: IntRange) = (this.first <= other.first && other.first <= this.last) || (other.first <= this.first && this.first <= other.last)
fun IntRange.contains(other: IntRange) = other.first in this && other.last in this
fun IntRange.intersection(other: IntRange) = max(this.first, other.first)..min(this.last, other.last)

/**
 * --- Day 22: Reactor Reboot ---
 */
class Day22(val data: String = "/day22.in") {

    enum class CubeState { ON, OFF }
    data class RebootStep(val state: CubeState, val cube: Cube)

    data class Cube(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
        fun size(): Long = xRange.count().toLong() * yRange.count().toLong() * zRange.count().toLong()
        fun contains(other: Cube)= xRange.contains(other.xRange) && yRange.contains(other.yRange) && zRange.contains(other.zRange)
        fun overlaps(other: Cube) = xRange.overlaps(other.xRange) && yRange.overlaps(other.yRange) && zRange.overlaps(other.zRange)

        fun intersection(other: Cube) =
            if (!overlaps(other)) null
            else Cube(xRange.intersection(other.xRange), yRange.intersection(other.yRange), zRange.intersection(other.zRange))
    }

    fun parseInputData(): List<RebootStep> {
        val regex = "(on|off) (x=)(-?\\d*)(\\.\\.)(-?\\d*),(y=)(-?\\d*)(\\.\\.)(-?\\d*),(z=)(-?\\d*)(\\.\\.)(-?\\d*)".toRegex()
        return resourceAsStrings(data).map { line ->
            val groups = regex.find(line)!!.groupValues
            RebootStep(
                CubeState.valueOf(groups[1].uppercase()),
                Cube(IntRange(groups[3].toInt(), groups[5].toInt()), IntRange(groups[7].toInt(), groups[9].toInt()), IntRange(groups[11].toInt(), groups[13].toInt()))
            )
        }
    }

    class Reactor() {
        // list of cubes added (switched ON)
        val switchedOnCubes = mutableListOf<Cube>()
        // list of cubes removed (switched OFF)
        val switchedOffCubes = mutableListOf<Cube>()

        fun processStep(rebootStep: RebootStep) {
            trace("reboot step: $rebootStep")
            val newCubesOn = mutableListOf<Cube>()
            val newCubesOff = mutableListOf<Cube>()
            val currentStepCube = rebootStep.cube

            if (CubeState.ON == rebootStep.state) {
                // add new cube
                newCubesOn.add(currentStepCube)
                // "restore" previous removed cubes
                newCubesOn.addAll(switchedOffCubes.mapNotNull { currentStepCube.intersection(it) })
                // add removedCube for each intersections between new cube and already added cubes
                newCubesOff.addAll(switchedOnCubes.map { currentStepCube.intersection(it) }.filterNotNull())
            } else {
                // add new "hole" cubes
                newCubesOff.addAll(switchedOnCubes.mapNotNull { currentStepCube.intersection(it) })
                // resurect previous "hole" cubes
                newCubesOn.addAll(switchedOffCubes.mapNotNull { currentStepCube.intersection(it) })
            }

            switchedOnCubes.addAll(newCubesOn)
            switchedOffCubes.addAll(newCubesOff)
        }
    }

    fun process(initPhase: Boolean = false): Long {
        var rebootSteps = parseInputData()

        if (initPhase) { // for part 1
            val initRange = Cube(-50..50, -50..50, -50..50)
            rebootSteps = rebootSteps.filter { initRange.contains(it.cube) }
        }
        val reactor = Reactor()
        for (rebootStep in rebootSteps) {
            reactor.processStep(rebootStep)
        }
        val addedCells = reactor.switchedOnCubes.sumOf { it.size() }
        val removedCells = reactor.switchedOffCubes.sumOf { it.size() }
        return addedCells - removedCells
    }

    fun part1() = process(initPhase = true)
    fun part2() = process(initPhase = false)

}

@OptIn(ExperimentalTime::class)
fun main() {
    val time = measureTime { println(" part1:" + Day22().part1()) }
    println(" time: ${time.inWholeMilliseconds} ms")
    val time2 = measureTime { println(" part2:" + Day22().part2()) }
    println(" time: ${time2.inWholeMilliseconds} ms")
}