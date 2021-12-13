package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Test {

    val sample = "/day01_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(7, Day01(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(5, Day01(sample).part2())
    }
}