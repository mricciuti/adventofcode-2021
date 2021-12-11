package mri.advent

val closingCharsMap = mapOf('(' to ')', '{' to '}', '<' to '>', '[' to ']')
val startTokens = arrayOf('(', '[', '{', '<')

val scoresPart1 = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
val scoresPart2 = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

// une ligne contenant des "chunks"
data class Chunks(val content: String) {
    var missingClosingChars = mutableListOf<Char>()

    // recherche le 1er qui rend le chunck invalide, s'il y en a
    fun findCorruptingChar(): Char? {
        val stack = mutableListOf<Char>()
        for (c: Char in content) {
            // new chunk
            if (startTokens.contains(c)) {
                stack.add(c)
            } else {
                // closing chunk
                if (stack.isNotEmpty()) {
                    val last = stack.last()
                    // corrupted chunk ! => exit
                    if (c != closingCharsMap[last]) {
                        return c
                    } else {
                        stack.removeLast()
                    }
                }
            }
        }
        // on stocke les closing-chars manquants
        this.missingClosingChars.addAll(stack.reversed().map { closingCharsMap[it]!! })
        return null
    }
}

private fun d10Part1(): Any {
    return resourceAsStrings("/src/main/resources/day10.in")
        .mapNotNull { Chunks(it).findCorruptingChar() }
        .sumOf { scoresPart1[it]!! }
}

fun computeScorePart2(missingChars: List<Char>): Long {
    var res: Long = 0
    (missingChars.indices).forEach { index ->
        res = res * 5
        res += scoresPart2[missingChars[index]]!!
    }
    return res
}

private fun d10Part2(): Any {
    val lines = resourceAsStrings("/src/main/resources/day10.in")
    val scores = mutableListOf<Long>()
    lines.map { Chunks(it) }
        .filter { it.findCorruptingChar() == null }
        .forEach {
            scores.add(computeScorePart2(it.missingClosingChars))
        }
    // find the completion string for each incomplete line, score the completion strings,
    // and sort the scores. What is the middle score?
    scores.sort()
    return scores[scores.size / 2]
}

fun main() {
    println(" part1:" + d10Part1())
    println(" part2:" + d10Part2())
}