package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13Test {

    val sample = "/day13_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(17, Day13(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(16, Day13(sample).part2())
    }
}