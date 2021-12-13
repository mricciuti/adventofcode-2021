package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day04Test {

    val sample = "/day04_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(4512, Day04(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(1924, Day04(sample).part2())
    }
}