package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {

    val sample = "/day03_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(198, Day03(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(230, Day03(sample).part2())
    }
}