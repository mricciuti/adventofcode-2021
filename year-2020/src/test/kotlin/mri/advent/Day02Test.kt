package mri.advent

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02Test {

    val data = "/day02_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(2, Day02(data).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(1, Day02(data).part2())
    }
}