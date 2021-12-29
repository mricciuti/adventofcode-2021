package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day21Test {

    val sample = "/day21_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(739785, Day21(4, 8).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(444356092776315, Day21(4, 8).part2())
    }
}