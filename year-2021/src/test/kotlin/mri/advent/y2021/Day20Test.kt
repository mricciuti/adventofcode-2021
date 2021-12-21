package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day20Test {

    val sample = "/day20_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(35, Day20(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(3351, Day20(sample).part2())
    }
}