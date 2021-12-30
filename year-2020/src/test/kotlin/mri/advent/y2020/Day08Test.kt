package mri.advent.y2020

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {

    val sample = "/day08_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(5, Day08(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(8, Day08(sample).part2())
    }
}