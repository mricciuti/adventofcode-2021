package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02Test {

    val sample = "/day02_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(150, Day02(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(900, Day02(sample).part2())
    }
}