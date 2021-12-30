package mri.advent.y2020

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {

    val sample = "/day06_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(11, Day06(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(6, Day06(sample).part2())
    }
}