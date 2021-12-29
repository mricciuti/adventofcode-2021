package mri.advent.y2021

import java.util.*
import kotlin.math.abs

/**
 * --- Day 23: Amphipod ---
 */

const val FREE_POS = '.'
val POD_MOVE_COST = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
val ROOM_POSITION = mapOf('A' to 2, 'B' to 4, 'C' to 6, 'D' to 8)

class Day23(val data: String = "/day23.in") {

    data class Coord(val x: Int, val y: Int) {
        fun distanceTo(other: Coord) = abs(this.x - other.x) + abs(this.y - other.y)
        override fun toString() = "$x,$y"
    }

    data class PodWithPos(val position: Coord, val type: Char) {
        constructor(x: Int, y: Int, type: Char) : this(Coord(x, y), type)
    }

    data class Hallway(val slots: ArrayList<Char> = ArrayList("...........".toList())) {
        fun isPathFree(from: Int, to: Int) = slots.slice(if (from < to) (from + 1..to) else (to until from)).all { it == FREE_POS }

        // return pods that can move from the hallway
        fun moveablePods(): List<PodWithPos> {
            val left = (0..1).map { pos -> pos to slots[pos] }.lastOrNull { it.second != FREE_POS }
            val right = (9..10).map { pos -> pos to slots[pos] }.firstOrNull { it.second != FREE_POS }
            val center = listOf(3, 5, 7).map { pos -> pos to slots[pos] }.filter { it.second != FREE_POS }
            return buildList {
                left?.let { add(PodWithPos(it.first, 0, it.second)) }
                center.forEach { add(PodWithPos(it.first, 0, it.second)) }
                right?.let { add(PodWithPos(it.first, 0, it.second)) }
            }
        }

        // reachable positions in hallway from given room
        fun freePositionsFromRoom(roomPosition: Coord): List<Coord> {
            return buildList {
                for (xLeft in roomPosition.x downTo 0) {
                    if (slots[xLeft] == FREE_POS) this.add(Coord(xLeft, 0)) else break
                }
                for (xRigth in roomPosition.x until slots.size) {
                    if (slots[xRigth] == FREE_POS) this.add(Coord(xRigth, 0)) else break
                }
            }.filter { !listOf(2, 4, 6, 8).contains(it.x) }
                .sortedBy { it.x }
        }

        override fun toString() = "[${slots.joinToString(", ") { it.toString() }}]"
        fun clone() = Hallway(ArrayList(slots))
    }

    data class Room(val type: Char, var slots: ArrayList<Char>) {
        val xPosition = ROOM_POSITION[type]!!
        fun isCompleted() = slots.all { it == type }
        fun hasInvalidPods() = slots.any { it != type && it != FREE_POS }
        fun accept(podType: Char) = !isCompleted() && podType == type && slots.all { it == podType || it == FREE_POS }
        fun moveablePod() = if (isCompleted()) null else slots.indices.firstOrNull { slots[it].isLetter() }?.let { PodWithPos(xPosition, it + 1, slots[it]) }
        fun firstFreeSlot() = slots.indexOfLast { it == FREE_POS }.let { if (it >= 0) Coord(xPosition, it + 1) else null }

        fun clone() = Room(type, ArrayList(slots))
        override fun toString() = slots.joinToString(",") { it.toString() }
    }

    data class GameState(val hallway: Hallway, val rooms: ArrayList<Room>) {

        private fun movePod(pod: PodWithPos, target: Coord): Pair<GameState, Int> {
            if (pod.position.y == 0) {  // move hallway -> room
                hallway.slots[pod.position.x] = FREE_POS
                rooms.first { it.xPosition == target.x }.slots[target.y - 1] = pod.type
            } else {                    // move room -> hallway
                hallway.slots[target.x] = pod.type
                rooms.first { it.xPosition == pod.position.x }.slots[pod.position.y - 1] = FREE_POS
            }
            return this to pod.position.distanceTo(target) * POD_MOVE_COST[pod.type]!!
        }

        fun nextStates(): List<Pair<GameState, Int>> {
            if (isCompleted())
                return listOf(this to 0)
            val result = mutableListOf<Pair<GameState, Int>>()

            // add possible moves from rooms to hallway
            rooms.filter { it.hasInvalidPods() }.forEach { room ->
                room.moveablePod()?.let { pod ->
                    hallway.freePositionsFromRoom(pod.position).forEach { target ->
                        result.add(this.clone().movePod(pod, target))
                    }
                }
            }
            // add possible moves from hallway to room
            hallway.moveablePods().forEach { pod ->
                // target root available  and path is clear ?
                rooms.firstOrNull { it.accept(pod.type) }?.let { room ->
                    if (hallway.isPathFree(pod.position.x, room.xPosition)) {
                        room.firstFreeSlot()?.let { target ->
                            result.add(this.clone().movePod(pod, target))
                        }
                    }
                }
            }
            return result
        }

        fun isCompleted() = rooms.all { it.isCompleted() }
        private fun clone() = GameState(hallway.clone(), ArrayList(rooms.map { it.clone() }))
    }

    data class GameStateCost(val gameState: GameState, val cost: Int = 0) : Comparable<GameStateCost> {
        override fun compareTo(other: GameStateCost) = this.cost.compareTo(other.cost)
    }

    fun process(extended: Boolean = false): Any {
        // load initial state
        val initialState = GameStateCost(parseInputData(extended))
        // Dijkstra setup
        val unvisitedStates = PriorityQueue<GameStateCost>()
        unvisitedStates.add(initialState)
        val visitedStates = mutableSetOf<GameStateCost>()
        val minStateCostMap = mutableMapOf<GameState, Int>()
        // search loop
        while (unvisitedStates.isNotEmpty()) {
            val stateToExplore = unvisitedStates.poll()
            visitedStates.add(stateToExplore)
            stateToExplore.gameState.nextStates()
                .map { GameStateCost(it.first, it.second) }
                .filter { !visitedStates.contains(it) }
                .forEach { newState ->
                    val newCost = newState.cost + stateToExplore.cost
                    if (newCost < minStateCostMap.computeIfAbsent(newState.gameState) { Integer.MAX_VALUE }) {
                        minStateCostMap[newState.gameState] = newCost
                        unvisitedStates.add(GameStateCost(newState.gameState, newCost))
                    }
                }
            debug("${stateToExplore.gameState.hallway} (${stateToExplore.cost}) - visited=${visitedStates.size} / ${unvisitedStates.size}")
        }
        return minStateCostMap.entries.first { it.key.isCompleted() }.value
    }

    fun parseInputData(extended: Boolean = false): GameState {
        val lines = resourceAsStrings(data).map { it.padEnd(13, ' ') }.toMutableList()
        if (extended) {
            lines.add(3, "  #D#C#B#A#  ")
            lines.add(4, "  #D#B#A#C#  ")
        }
        val roomLines = lines.filter { it.any { it.isLetter() } }
        return GameState(Hallway(), ArrayList((0 until 4).map { rIndex ->
            Room('A' + rIndex, ArrayList(roomLines.map { it[3 + rIndex * 2] }.toList()))
        }))
    }
}

fun main() {
    println(" part1:" + Day23().process())
    println(" part2:" + Day23().process(extended = true))
}