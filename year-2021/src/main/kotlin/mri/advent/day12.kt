package mri.advent

/**
 *   Day 12: Passage Pathing ---
 */
class Day12(val data: String) {

    data class Cave(val name: String) {
        val connectedCaves: MutableList<Cave> = mutableListOf()
        fun isBigCave() = name.toCharArray().all { it.isUpperCase() }
        fun isEndCave() = name == "end"
        fun isStartCave() = name == "start"
        fun connectTo(other: Cave) {
            connectedCaves.add(other)
            other.connectedCaves.add(this)
        }
    }

    data class Path(val origin: Cave) {
        private val nodes = mutableListOf(origin)
        var canVisitSmallCaveTwice = true

        fun add(node: Cave): Path {
            if (!node.isBigCave() && nodes.contains(node)) {
                canVisitSmallCaveTwice = false
            }
            nodes.add(node)
            return this
        }

        fun expand(candidateAllowed: (path: Path, candicate: Cave) -> Boolean): List<Path> {
            if (this.isComplete()) {
                return mutableListOf(this)
            }
            val paths = mutableListOf<Path>()
            nodes.last().connectedCaves
                .filter { candidateAllowed(this, it) }
                .forEach { paths.addAll(this.clone().add(it).expand(candidateAllowed)) }
            return paths
        }

        private fun clone(): Path {
            val path = Path(this.origin)
            (1 until nodes.size).forEach { path.add(nodes[it]) }
            return path
        }

        fun alreadyVisited(cave: Cave) = nodes.contains(cave)
        fun isComplete() = nodes.last().isEndCave()
    }

    // input file => Cave nodes list
    private fun parseCaveNodes(): List<Cave> {
        val nodes = mutableMapOf<String, Cave>()
        val edges = resourceAsStrings(data).map { Pair(it.split("-").first(), it.split("-").last()) }
        edges.forEach { edge ->
            nodes.getOrPut(edge.first) { Cave(edge.first) }
                .connectTo(nodes.getOrPut(edge.second) { Cave(edge.second) })
        }
        return nodes.values.toList()
    }

    private fun countPaths(caveAllowedPredicate: (Path, Cave) -> Boolean): Int {
        val nodes = parseCaveNodes()
        // initial paths, from Start node
        val startingNode = nodes.first { it.isStartCave() }
        val firstPaths = startingNode.connectedCaves.map { Path(startingNode).add(it) }
        // expand all paths
        val allPaths = mutableListOf<Path>()
        firstPaths.forEach { allPaths.addAll(it.expand(caveAllowedPredicate)) }
        return allPaths.size
    }

    fun part2(): Any {
        /**
         * Big caves can be visited any number of times, a single small cave can be visited at most twice,
         * and the remaining small caves can be visited at most once. However, the caves named start and end can only be visited exactly once each
         */
        return countPaths { path, cave ->
            cave.isEndCave() || cave.isBigCave()
                    || (!cave.isStartCave() && (path.canVisitSmallCaveTwice || !path.alreadyVisited(cave)))
        }
    }

    fun part1(): Any {
        /** So, all paths you find should visit small caves at most once, and can visit big caves any number of times. */
        return countPaths { path, cave ->
            cave.isBigCave() || cave.isEndCave() || !path.alreadyVisited(cave)
        }
    }
}

fun main() {
    val data = "/day12.in"
    println(" part1:" + Day12(data).part1())
    println(" part2:" + Day12(data).part2())
}