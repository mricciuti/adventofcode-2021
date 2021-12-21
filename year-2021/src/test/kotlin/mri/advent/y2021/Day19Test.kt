package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day19Test {

    val sample = "/day19_sample.in"
    val sample_short = "/day19_sample_short.in"

    @Test
    fun `test part1`() {
        assertEquals(79, Day19(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(3621, Day19(sample).part2())
    }

    @Test
    fun testManhattanDistance() {
        val first = Day19.Coord3D(1105,-1205,1229)
        val second = Day19.Coord3D(-92,-2380,-20)
        assertEquals(3621, first.manhattanDistanceTo(second))
    }
}