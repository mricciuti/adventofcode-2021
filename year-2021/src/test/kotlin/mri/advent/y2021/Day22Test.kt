package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.*

class Day22Test {

    val sample = "/day22_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(39, Day22(sample).part1())
    }


    @Test
    fun `test part1_large`() {
        assertEquals(590784, Day22("/day22_sample_large.in").part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(2758514936282235L, Day22("/day22_sample_part2.in").part2())
    }

    @Test
    fun `test range overlaps`() {
        assertTrue { (0..10).overlaps(5..5) }
        assertTrue { (-5..5).overlaps(3..5) }
        assertFalse { (0..10).overlaps(11..15) }
        assertFalse { (-3..10).overlaps(-10..-4) }
    }
    @Test
    fun `test range3D overlaps`() {
        val range1 = Day22.Cube(0..1, 0..1, 0..1)

        assertTrue { range1.overlaps(Day22.Cube(1..2, 0..1,0..1)) }
        assertTrue { range1.overlaps(Day22.Cube(1..2, 0..1,-1..0)) }

        assertFalse { range1.overlaps(Day22.Cube(2..3, 0..1,0..1)) }
        assertFalse { range1.overlaps(Day22.Cube(0..1, 0..1,-2..-1)) }
    }

    @Test
    fun `test range3d intersections`() {
        val range1 = Day22.Cube(0..2, 0..2, 0..2)
        assertEquals(27, range1.size())
        val intersect1 = range1.intersection(Day22.Cube(2..4, 0..2, 2..4))
        assertEquals(3, intersect1!!.size())
        assertEquals(Day22.Cube(2..2, 0..2, 2..2), intersect1)

        // contained cube
        val intersect2 = range1.intersection(Day22.Cube(1..1, 1..1, 1..1))
        assertEquals(1, intersect2!!.size())

        // no intersection at all
        val intersect3 = range1.intersection(Day22.Cube(5..10, 5..10, 5..10))
        assertNull(intersect3)
    }
}