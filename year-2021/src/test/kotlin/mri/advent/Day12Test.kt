package mri.advent

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {

    val data = "/day12_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(10, Day12(data).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(36, Day12(data).part2())
    }
}