package mri.advent.y2020

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {

    val sample = "/day09_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(127, Day09(sample).part1(5))
    }

    @Test
    fun `test part2`() {
        assertEquals(62L, Day09(sample).part2(5))
    }
}