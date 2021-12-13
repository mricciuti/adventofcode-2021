package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {

    val sample = "/day08_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(26, Day08(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(61229L, Day08(sample).part2())
    }
}