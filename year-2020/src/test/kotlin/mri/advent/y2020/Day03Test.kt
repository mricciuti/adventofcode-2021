package mri.advent.y2020

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {

    val sample = "/day03_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(7, Day03(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(336, Day03(sample).part2())
    }
}