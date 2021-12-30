package mri.advent.y2020

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05Test {

    val sample = "/day05_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(357, Day05(sample).part1())
    }

}