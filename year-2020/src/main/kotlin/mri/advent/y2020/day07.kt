package mri.advent.y2020

/**
 * --- Day 07: Handy Haversacks  ---
 */
class Day07(val data: String = "/day07.in") {

    private val bags = mutableMapOf<String, Bag>()

    data class Bag(val type: String) {
        val parentBags: MutableSet<Bag> = mutableSetOf()
        val contains: MutableSet<Contraint> = mutableSetOf()

        fun addConstraint(child: Contraint) {
            contains.add(child)
            child.bag.parentBags.add(this)
        }

        fun count(): Long {
            return 1 + contains.sumOf { it.amount * it.bag.count() }
        }
    }

    data class Contraint(val amount: Int, val bag: Bag)

    private fun parseBagRules() {
        val regex = "(.*) bags contain (.*)\\.".toRegex()
        resourceAsStrings(data).map { line ->
            regex.find(line)?.let {
                it.groupValues.let { grp ->
                    val bag = bags.computeIfAbsent(grp[1]) { Bag(grp[1]) }
                    grp[2].split(",").asSequence().map { it.trim() }
                        .filter { !it.contains("no other bag") }
                        .map { it.split(" ") }
                        .map { it[0].toInt() to it.subList(1, it.size - 1).joinToString(" ") }
                        .map { pair -> Contraint(pair.first, bags.computeIfAbsent(pair.second) { Bag(pair.second) }) }.toList()
                        .forEach { c -> bag.addConstraint(c) }
                }
            }
        }
    }

    fun part1(): Any {
        parseBagRules()

        val toVisit = mutableListOf<Bag>().apply { bags["shiny gold"]?.let { this.add(it) } }
        val visited = mutableSetOf<Bag>()
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (!visited.contains(current)) {
                current.parentBags.forEach { toVisit.add(it) }
                visited.add(current)
            }
        }
        return visited.size - 1
    }

    fun part2(): Long {
        parseBagRules()
        return bags["shiny gold"]?.let { return it.count() - 1 }
            ?: throw IllegalStateException("bag not found")
    }
}

fun main() {
    println(" part1:" + Day07().part1())
    println(" part2:" + Day07().part2())
}