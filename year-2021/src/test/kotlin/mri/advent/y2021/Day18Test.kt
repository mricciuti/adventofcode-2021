package mri.advent.y2021

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day18Test {

    val sample = "/day18_sample.in"

    @Test
    fun testParseSimple() {
        val day = Day18("")
        day.parseNumber("[1,2]")
        day.parseNumber("[[1,2],3]")
        day.parseNumber("[9,[8,7]]")
        day.parseNumber("[[1,9],[8,5]]")
    }

    @Test
    fun `test part1`() {
        assertEquals(4140, Day18("/day18_sample.in").part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(3993, Day18(sample).part2())
    }

    @Test
    fun testSplit() {
        val number = Day18("").parseNumber("9")
        number.split()
        assertTrue { number.left != null }
    }

    @TestFactory
    fun testReduce() = listOf(
        "[[[[[9,8],1],2],3],4]" to "[[[[0,9],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]" to "[7,[6,[5,[7,0]]]]",
        "[[6,[5,[4,[3,2]]]],1]" to "[[6,[5,[7,0]]],3]",
        "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("reducing $input should result in $expected") {
            assertEquals(expected, Day18("").parseNumber(input).reduce().toString())
        }
    }

}