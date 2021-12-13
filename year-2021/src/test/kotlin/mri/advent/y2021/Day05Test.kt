package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05Test {

    val sample = "/day05_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(5, Day05(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(12, Day05(sample).part2())
    }
}