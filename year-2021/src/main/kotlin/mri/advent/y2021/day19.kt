package mri.advent.y2021

import kotlin.math.abs

/**
 * --- Day 19:  ---
 */
class Day19(val data: String = "/day19.in") {


    data class Coord3D(val x: Int, val y: Int, val z: Int) {

        fun opposite() = Coord3D(-x, -y, -z)
        fun plus(other: Coord3D) = Coord3D(x + other.x, y + other.y, z + other.z)
        fun minus(other: Coord3D) = Coord3D(x - other.x, y - other.y, z - other.z)
        fun manhattanDistanceTo(other: Coord3D) = abs(other.x - x) + abs(other.y - y) + abs(other.z - z)

        // see https://www.euclideanspace.com/maths/algebra/matrix/transforms/examples/index.htm
        private val rotations = listOf<(Coord3D) -> Coord3D>(
            { coord -> with(coord) { Coord3D(x, y, z) } },
            { coord -> with(coord) { Coord3D(x, -y, -z) } },
            { coord -> with(coord) { Coord3D(x, -z, y) } },
            { coord -> with(coord) { Coord3D(-y, -z, x) } },
            { coord -> with(coord) { Coord3D(-x, -z, -y) } },
            { coord -> with(coord) { Coord3D(y, -z, -x) } }
        )
        private val faceUpDirs = listOf<(Coord3D) -> Coord3D>(
            { coord -> with(coord) { Coord3D(x, y, z) } },
            { coord -> with(coord) { Coord3D(-y, x, z) } },
            { coord -> with(coord) { Coord3D(-x, -y, z) } },
            { coord -> with(coord) { Coord3D(y, -x, z) } }
        )

        // return this coordinate in 24 possible scanner orientations
        fun orientations() = rotations.flatMap { rotate -> faceUpDirs.map { faceUp -> faceUp(rotate(this)) } }

        override fun toString() = "[$x,$y,$z]"
    }

    data class Scanner(val id: Int) {
        // beacons list, store in each of their 24 possible coordinates
        val beacons = Array(24) { mutableListOf<Coord3D>() }

        // real position (relative to scanner  #0 )
        var position: Coord3D? = null
        // real beacon coordinates (in scanner #0 coordinate system)
        var knownBeacons: List<Coord3D> = listOf()

        fun addBeacon(coord: Coord3D) {
            coord.orientations().forEachIndexed { index, coord3D -> beacons[index].add(coord3D) }
        }

        /**
         * Try to detect give other scanner , by testing if this overlaps other scanner.
         * => testing all 24 possible orientation for other scanner, find 12 beacon that match
         */
        fun tryDetect(other: Scanner): Boolean {
            if (this.position == null) throw IllegalStateException("source scanner not yet detected")
            val myBeacons = knownBeacons
            // test all possible beacon orientations fot other scanner
            other.beacons.indices.forEach { orientationIndex ->
                val otherBeacons = other.beacons[orientationIndex]
                beacons.indices.forEach { myIdx ->
                    otherBeacons.indices.forEach { otherIdx ->
                        val coordDelta = otherBeacons[otherIdx].minus(myBeacons[myIdx])
                        val commonBeacons = myBeacons.map { it.plus(coordDelta) }.intersect(otherBeacons)
                        if (commonBeacons.size >= 12) {
                            other.position = coordDelta.opposite()
                            debug("   -> scanner ${other} identified by ${this.id} with delta $coordDelta")
                            other.knownBeacons = other.beacons[orientationIndex].map { it.minus(coordDelta) }
                            return true
                        }
                    }
                }
            }
            return false
        }

        override fun toString(): String {
            return "scanner #$id - position: ${position?: "unknown"}"
        }
    }

    private fun parseCoord(line: String): Coord3D = line.split(",").map { it.toInt() }.let { return Coord3D(it[0], it[1], it[2]) }

    private fun parseInputData(): List<Scanner> {
        val scanners = mutableListOf<Scanner>()
        var current = Scanner(scanners.size)
        resourceAsStrings(data).forEach { line ->
            if (line.isBlank()) {
                scanners.add(current)
                current = Scanner(scanners.size)
            } else if (!line.startsWith("---")) {
                current.addBeacon(parseCoord(line))
            }
        }
        scanners.add(current)
        return scanners
    }

    private fun locateScanners(): List<Scanner> {
        val scanners = parseInputData()

        // set scanner #0 as reference
        scanners[0].position = Coord3D(0, 0, 0)
        scanners[0].knownBeacons = scanners[0].beacons[0]
        val scannersToTest = mutableListOf(scanners[0])

        // detection processing loop.
        while(scannersToTest.isNotEmpty()) {
            val scannerToTest = scannersToTest.removeFirst()
            scanners.filter { it.position == null }.forEach { other ->
                trace("test scanners #${scannerToTest.id} and ${other.id}")
                if (scannerToTest.tryDetect(other)) {
                    scannersToTest.add(other)
                }
            }
        }
        return scanners
    }

    /**
     * Assemble the full map of beacons. How many beacons are there?
     */
    fun part1(): Any {
        val scanners = locateScanners()

        // count all beacons
        val allBeacons = mutableSetOf<Coord3D>()
        scanners.forEach { allBeacons.addAll( it.knownBeacons) }
        allBeacons.sortedBy { it.x }.forEach { info(it.toString()) }
        return allBeacons.size
    }

    /**
     * What is the largest Manhattan distance between any two scanners?
     */
    fun part2(): Any {
        val scanners = locateScanners()
        val distances = mutableListOf<Pair<Pair<Scanner, Scanner>, Int>>()
        scanners.indices.forEach { firstIdx ->
            (firstIdx + 1 until scanners.size).forEach { secondIdx ->
                val first = scanners[firstIdx]
                val second = scanners[secondIdx]
                distances.add(Pair(Pair(first,second), first.position!!.manhattanDistanceTo(second.position!!)))
            }
        }
        return distances.maxOf { it.second }
    }
}

fun main() {
    println(" part1:" + Day19().part1()) //    376
    println(" part2:" + Day19().part2()) //  10772
}