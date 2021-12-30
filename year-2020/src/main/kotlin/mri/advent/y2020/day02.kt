package mri.advent.y2020

/**
 * --- Day 2: Password Philosophy ---
 */
class Day02(val data: String) {

    data class Constraint(val char: Char, val first: Int, val second: Int)
    data class RecordLine(val constraint: Constraint, val password: String)

    private fun parseRecord(line: String): RecordLine {
        "(\\d+)-(\\d+) (.+): (\\w+)".toRegex().find(line)?.let {
            return RecordLine(Constraint(it.groupValues[3][0], it.groupValues[1].toInt(), it.groupValues[2].toInt() ), it.groupValues[4])
        } ?: throw IllegalArgumentException("invalid line: $line")
    }

    /**
     * Each policy actually describes two positions in the password,
     * where 1 means the first character, 2 means the second character, and so on.
     * (Be careful; Toboggan Corporate Policies have no concept of "index zero"!) Exactly one of these positions must contain the given letter.
     * Other occurrences of the letter are irrelevant for the purposes of policy enforcement.
     */
    fun part2(): Any {
        return resourceAsStrings(data).filter{ it.isNotEmpty()}
            .map { parseRecord(it)}
            .count { rec ->
                rec.password.length >= rec.constraint.second
                        &&  (listOf(rec.password[rec.constraint.first - 1], rec.password[rec.constraint.second - 1])
                            .count { it == rec.constraint.char }) == 1

            }
    }

    /**
     * The password policy indicates the lowest and highest number of times a given letter must appear for the password to be valid.
     * For example, 1-3 a means that the password must contain  at least 1 time and at most 3 times.
     */
    fun part1(): Any {
        return resourceAsStrings(data).filter{ it.isNotEmpty()}
            .map { parseRecord(it)}
            .count { rec -> rec.password.count { it == rec.constraint.char } in rec.constraint.first..rec.constraint.second }
    }
}

fun main() {
    val data = "/day02.in"
    println(" part1:" + Day02(data).part1())
    println(" part2:" + Day02(data).part2())
}
