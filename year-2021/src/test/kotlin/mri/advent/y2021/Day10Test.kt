package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10Test {

    val sample = "/day10_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(26397, Day10(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(288957L, Day10(sample).part2())
    }
}