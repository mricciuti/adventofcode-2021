package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {

    val sample = "/day06_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(5934L, Day06(sample, 1_000_000).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(26984457539, Day06(sample, 1_000_000).part2())
    }
}