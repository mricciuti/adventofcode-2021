package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14Test {

    val sample = "/day14_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(1588, Day14(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(2188189693529, Day14(sample).part2())
    }
}