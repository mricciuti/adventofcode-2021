package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day25Test {

    val sample = "/day25_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(58, Day25(sample).part1())
    }

//    @Test
    fun `test part2`() {
        assertEquals(0, Day25(sample).part2())
    }
}