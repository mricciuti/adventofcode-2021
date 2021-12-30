package mri.advent.y2020

/**
 * --- Day 06:  Custom Customs---
 */
class Day06(val data: String = "/day06.in") {

    private fun parseGroups(): List<List<String>> {
        return resourceAsText(data).split(("\r\n\r\n")).map { it.split("\r\n") }
    }

    fun part1(): Any {
        return parseGroups().sumOf { group ->
            val answers = HashSet<Char>()
            group.forEach { it.forEach { answers.add(it) } }
            answers.count()
        }
    }

    fun part2(): Any {
        return parseGroups().sumOf { group ->
            var answers: Set<Char> = group[0].toSet()
            repeat(group.size - 1) { idx ->
                answers = answers.intersect(group[idx + 1].toSet())
            }
            answers.count()
        }
    }
}

fun main() {
    println(" part1:" + Day06().part1())
    println(" part2:" + Day06().part2())
}