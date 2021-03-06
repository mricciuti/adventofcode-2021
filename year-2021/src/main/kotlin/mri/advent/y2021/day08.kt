package mri.advent.y2021

class Day08(val data: String = "day08.in") {

    // 1 ligne dans les input:    10 signal patterns | 4 digit output values
    data class Display(val patterns: Collection<CharArray>, val digits: Collection<CharArray>) {
        fun getBySegments(segCount: Int) = patterns.filter { it.size == segCount }

        companion object {
            fun parse(line: String): Display {
                val parts = line.split("|").map { it.trim() }
                return Display(
                    parts[0].split(" ").map { it.toCharArray().sortedArray() },
                    parts[1].split(" ").map { it.toCharArray().sortedArray() }
                )
            }
        }
    }

    // qq extension-functions utiles  ----------------

    // test if current charArrayincludes all other chararray chars - ex: 0 includes 1 & 7 ,  but not  2, 3, 4, 5, 6, 8 ni 9
    fun CharArray.includes(other: CharArray) = other.none { !this.contains(it) }

    // get common chars between current array and given array -  eg.  common(7, 1) returns [ 'c', 'f' ]
    fun CharArray.common(other: CharArray) = this.filter { other.contains(it) }.toCharArray()
    fun CharArray.equals(other: Any?) = other is CharArray && String(this.sortedArray()).equals(String(other.sortedArray()))

    // calcule le résultat pour  1 Display
    private fun processDisplay(display: Display): Long {
        val p1 = display.getBySegments(2)[0]
        val p4 = display.getBySegments(4)[0]
        val l5 = display.getBySegments(5)  // patterns de longueur 5 (2, 3, 5)
        val l6 = display.getBySegments(6)  // patterns de longueur 6 (0, 6, 9)
        val p7 = display.getBySegments(3)[0]
        val p8 = display.getBySegments(7)[0]

        // step 1 : Segments c & f ------------
        val cf = p7.common(p1)

        // step 2 : groupe 0, 6, 9 --------------
        //  => trouver 6 : ( seul sans c+f )
        val p6 = l6.first { !it.includes(cf) }
        //  => reste 0,9 :  9 inclue 4,  mais pas 0
        val p09 = l6.filter { !it.equals(p6) }
        val p9 = p09.first { it.includes(p4) }
        val p0 = p09.first { !it.equals(p9) }

        // step 3 : groupe 2, 3, 5 -------------
        //  => trouver 3 (seul avec  c+f)
        val p3 = l5.first { it.includes(cf) }
        //  reste 2, 5: 6 inclues 5 (mais pas 2) versus 6 : 6 =  5 + e  => trouve 5 et e !
        val p25 = l5.filter { !it.equals(p3) }
        val p5 = p25.first { p6.includes(it) }
        val p2 = p25.first { !it.equals(p5) }

        // mapping Pattern => valeur
        val patternValue = mapOf(
            String(p0) to 0, String(p1) to 1, String(p2) to 2, String(p3) to 3, String(p4) to 4,
            String(p5) to 5, String(p6) to 6, String(p7) to 7, String(p8) to 8, String(p9) to 9,
        )
        val output = display.digits.map { String(it) }.map { patternValue[it]!!.toString() }
        return output.joinToString("") { it }.toLong()
    }

    fun part2(): Any {
        return resourceAsStrings(data)
            .map { Display.parse(it) }
            .map { processDisplay(it) }
            .sum()
    }

    fun part1(): Any {
        return resourceAsStrings(data)
            .map { Display.parse(it) }
            .sumOf { it.digits.count { listOf(2, 3, 4, 7).contains(it.size) } }
    }

}

fun main() {
    println(" part1:" + Day08().part1())
    println(" part2:" + Day08().part2())
}