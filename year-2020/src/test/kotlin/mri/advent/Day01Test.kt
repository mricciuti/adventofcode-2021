package mri.advent

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Test {

    val data = "/day01_sample.in"
    @Test
    fun `test part1`() {
        assertEquals(514579L, Day01(data).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(241861950L, Day01(data).part2())
    }
}
