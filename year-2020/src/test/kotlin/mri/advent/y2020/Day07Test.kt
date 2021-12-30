package mri.advent.y2020

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07Test {

    val sample = "/day07_sample.in"
    val sample2 = "/day07_sample2.in"

    @Test
    fun `test part1`() {
        assertEquals(4, Day07(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(32L, Day07(sample2).part2())
    }
}