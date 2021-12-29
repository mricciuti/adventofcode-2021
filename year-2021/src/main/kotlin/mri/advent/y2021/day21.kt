package mri.advent.y2021

/**
 * --- Day 21:  ---
 */
class Day21(val p1Position: Int, val p2Position: Int) {

    // Part 1 -----------------------------------------------------------------
    data class Dice(var value: Int = 1, var rollCount: Int = 0) {
        // roll algo part 1 - deterministic dice : increases from 1 to 100 then reset to 1
        fun roll(): Int {
            val current = value
            value = if (value == 100) 1 else value + 1
            rollCount++
            return current
        }
    }

    data class Player(val id: Int, var position: Int, var score: Int = 0) {
        fun turn(dice: Dice) {
            repeat(3) { this.position += dice.roll() }
            position = 1 + (position -1 ) % 10
            score += (position)
        }

        override fun toString() = "player #$id: position=${position + 1}, score=$score"
    }

    fun part1(): Any {
        val maxScore = 1000
        val player1 = Player(1, p1Position )
        val player2 = Player(2, p2Position )
        val dice = Dice()
        while (true) {
            // steps:  player 1 moves, player 2 moves, compute score.
            player1.turn(dice)
            if (player1.score >= maxScore) return player2.score * dice.rollCount
            player2.turn(dice)
            if (player2.score >= maxScore) return player1.score * dice.rollCount
            debug("Current state: player1=$player1 , player2=$player2 , dice=$dice")
        }
    }

    // Part 2 ======================================================

    data class DiceGameState(var pos1: Int, var score1: Int = 0, var pos2: Int, var score2: Int = 0) {
        var playerOneTurn = true
        var currentPlayerTurn = 0 // current player roll turn: 0 until 3

        // return index of winner ; null if not yet won
        fun winnerIndex(): Int? = if (score1 >= 21) 0 else if (score2 >= 21) 1 else null
        fun won(): Boolean = winnerIndex() != null

        // create 3 new game states from current state ( for dice roll values 1, 2 or 3)
        fun nextRoll(): List<DiceGameState> {
            val newStates = mutableListOf<DiceGameState>()
            (1..3).forEach { rollValue ->
                val game = this.clone()
                game.update(rollValue)
                newStates.add(game)
            }
            return newStates
        }

        fun switchCurrentPlayer() {
            if (playerOneTurn) {
                score1 += pos1
            } else {
                score2 += pos2
            }
            playerOneTurn = !playerOneTurn
            currentPlayerTurn = 0
        }

        fun update(diceRollValue: Int) {
            // move current player
            if (playerOneTurn) {
                pos1 += diceRollValue
                pos1 = 1 + (pos1 -1 ) % 10
            } else {
                pos2 += diceRollValue
                pos2 = 1 + (pos2 -1 ) % 10
            }
            // finalize, if player rolled 3 times
            if (++currentPlayerTurn >= 3) {
                switchCurrentPlayer()
            }
        }

        fun clone(): DiceGameState {
            val clone = this.copy()
            clone.playerOneTurn = this.playerOneTurn
            clone.currentPlayerTurn = this.currentPlayerTurn
            return clone
        }
    }

    // creates list of all possible game universes
    fun getAllGameStates(initialState: DiceGameState): Map<DiceGameState, Long> {
        var statesCountCache = hashMapOf<DiceGameState, Long>()
        statesCountCache.put(initialState, 1L)
        while (statesCountCache.filter { !it.key.won() }.isNotEmpty()) {
            // reduced cache
            val updatedCache = hashMapOf<DiceGameState, Long>()
            statesCountCache.forEach { gameState, count ->
                if (!gameState.won()) {
                    // expand with new game combinations
                    val childrenStates = gameState.nextRoll()
                    childrenStates.forEach { child ->
                        updatedCache[child] = updatedCache.computeIfAbsent(child) { 0L } + count
                    }
                } else {
                    updatedCache[gameState] = updatedCache.computeIfAbsent(gameState) { 0L } + count
                }
            }
            statesCountCache = updatedCache
        }
        return statesCountCache
    }

    fun part2(): Long {
        // 3-side dice, each roll creates 3 new dimensions (dice outcome 1, 2 or 3)
        // players wins at 21pts
        // Using your given starting positions, determine every possible outcome.
        // Find the player that wins in more universes; in how many universes does that player win?
        val initialState = DiceGameState(p1Position, 0, p2Position, 0)
        val allGameStates = getAllGameStates(initialState)
        val gamesCountByWinner = allGameStates
            .map { Pair(it.key.winnerIndex()!!, it.value) }
            .groupBy { it.first } // group by player index

        return gamesCountByWinner
            .map { entry -> entry.value.sumOf { it.second } } // sum winning universes
            .maxOf { it }
    }
}

fun main() {
    // lazy & quick input parsing :)
    // Player 1 starting position: 8
    // Player 2 starting position: 7
    println(" part1:" + Day21(8, 7).part1())
    println(" part2:" + Day21(8, 7).part2())
}
