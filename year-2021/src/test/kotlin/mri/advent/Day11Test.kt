package mri.advent

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11Test {

    val data = "/day11_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(1656, Day11(data).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(195, Day11(data).part2())
    }
}

/*
5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526
 */