package mri.advent.y2020

/**
 * --- Day 04:  Passport Processing ---
 */
val EXPECTED_FIELDS = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

class Day04(val data: String = "/day04.in") {

    data class Passport(val fields: Map<String, String>) {
        constructor(fieldsList: List<String>) : this(fieldsList.map { it.split(":") }.associate { it[0] to it[1] })

        private fun field(key: String) = fields[key]!!

        fun isValid(): Boolean {
            return EXPECTED_FIELDS.none { fields[it] == null }
        }

        private fun validHeight() = field("hgt").let{
            if (it.endsWith("cm")) (it.removeSuffix("cm").toInt() in 150 .. 193)
            else it.endsWith("in") && (it.removeSuffix("in").toInt() in 59 .. 76)
        }
        private fun validHairColor() = field("hcl").let{
            it.startsWith("#") && it.length == 7 && it.substring(1).all { it in 'a' .. 'f' || it in '0' .. '9' }
        }

        fun isValid2(): Boolean {
            return isValid()
                    && field("byr").toInt() in 1920..2002
                    && field("iyr").toInt() in 2010..2020
                    && field("eyr").toInt() in 2020..2030
                    && validHeight()
                    && validHairColor()
                    && listOf("amb", "blu", "brn",  "gry",  "grn",  "hzl",  "oth").contains(field("ecl"))
                    && field("pid").let{ it.length == 9 && it.all { it.isDigit() }}
        }
    }

    private fun parseInput(): List<Passport> {
        return Day04::class.java.getResource(data).readText()
            .split(("\r\n\r\n"))
            .map { it.replace("\r\n", " ") }
            .map { it.split(" ") }
            .map { Passport(it) }
    }

    fun part2(): Any {
        val passports = parseInput()
        return passports.count { it.isValid2() }
    }

    fun part1(): Any {
        val passports = parseInput()
        return passports.count { it.isValid() }
    }
}

fun main() {
    println(" part1:" + Day04().part1())
    println(" part2:" + Day04().part2())
}