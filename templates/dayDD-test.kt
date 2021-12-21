package mri.advent.yYYYY

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DayDDTest {

    val sample = "/dayDD_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(0, DayDD(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(0, DayDD(sample).part2())
    }
}