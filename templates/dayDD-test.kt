package mri.advent

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DayDDTest {

    val data = "/dayDD_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(0, DayDD(data).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(0, DayDD(data).part2())
    }
}