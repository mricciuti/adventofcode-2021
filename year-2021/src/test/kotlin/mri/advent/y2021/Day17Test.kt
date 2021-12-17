package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day17Test {

    val sample = "/day17_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(45, Day17(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(112, Day17(sample).part2())
    }

    @Test
    fun `range contains test` (){
        val range = Day17.Range(Day17.Coord(207, -63), Day17.Coord(263, -115))
        assertTrue { range.contains(Day17.Coord(210, -100)) }
    }

    @Test
    fun `parseTargetRange`() {
        val range = Day17().parseTargetRange("target area: x=20..30, y=-10..-5")
        assertEquals(Day17.Coord(20, -5), range.upLeft )
        assertEquals(Day17.Coord(30, -10), range.bottomRight )
    }
}