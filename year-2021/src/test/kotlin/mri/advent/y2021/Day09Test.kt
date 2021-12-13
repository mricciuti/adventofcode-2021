package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {

    val sample = "/day09_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(15, Day09(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(1134, Day09(sample).part2())
    }
}