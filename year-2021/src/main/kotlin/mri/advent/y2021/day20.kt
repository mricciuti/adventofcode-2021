package mri.advent.y2021

import java.math.BigInteger

/**
 * --- Day 20:  Trench Map ---
 */
class Day20(val data: String = "/day20.in") {

    data class Image(val pixels: Array<BooleanArray>) {

        private fun pixelAt(row: Int, col: Int, default: Boolean): Boolean {
            return if (row in pixels.indices && col in pixels[0].indices) pixels[row][col] else default
        }

        private fun getAroundPixels(row: Int, col: Int, newPixelValue: Boolean): String {
            val chars = mutableListOf<Char>()
            for (y in row -1 .. row + 1) {
                for (x in col -1 .. col + 1) {
                    chars.add( if ( pixelAt(y, x, newPixelValue)) '1' else '0')
                }
            }
            return String(chars.toCharArray())
        }

        private fun binaryToDecimal(binary: String) = BigInteger(binary, 2).toString(10).toLong()

        fun transform(algorithm: String, iteration: Int): Image {
            // tricky : if algo starts with '#' and ends with '.' => flipping image background (even iterations)
            val backGroundLightOn = iteration % 2 == 1 && algorithm.first() == '#' && algorithm.last() == '.'

            // expand image border + 1 each direction
            val output = Array(pixels.size + 2) { BooleanArray(pixels[0].size + 2) }
            for (r in output.indices) {
                for (c in output[0].indices) {
                    val area = getAroundPixels(r - 1, c - 1, backGroundLightOn)
                    val index = binaryToDecimal(area).toInt()
                    output[r][c] = algorithm[index] == '#'
                }
            }
            return Image(output)
        }
        override fun toString() = pixels.joinToString("\n") { it.joinToString("") { light -> if (light) "#" else "." } }
    }

    private fun enhance(times: Int): Image {
        val inputLines = resourceAsStrings(data)
        val algo = inputLines[0]
        val pixels = Array(inputLines.size - 2) { row -> BooleanArray(inputLines[2].length) { col -> inputLines[2 + row][col] == '#' } }
        var image = Image(pixels)
        repeat(times) { iteration ->
            trace(image.toString())
            image = image.transform(algo, iteration)
        }
        trace(image.toString())
        return image
    }

    fun part1() = enhance(2).pixels.sumOf { it.count { it } }
    fun part2() = enhance(50).pixels.sumOf { it.count { it } }
}

fun main() {
    println(" part1:" + Day20().part1())
    println(" part2:" + Day20().part2())
}