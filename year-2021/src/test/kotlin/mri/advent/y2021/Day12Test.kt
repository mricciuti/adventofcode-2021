package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {

    val sample = "/day12_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(10, Day12(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(36, Day12(sample).part2())
    }
}