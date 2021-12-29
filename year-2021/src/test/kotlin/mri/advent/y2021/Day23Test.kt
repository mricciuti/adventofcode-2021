package mri.advent.y2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class Day23Test {

    val sample = "/day23_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(12521, Day23(sample).process())
    }

    @Test
    fun `test part2`() {
        assertEquals(44169, Day23(sample).process(extended = true))
    }

    @Test
    fun `test room is completed`() {
        assertFalse { room('A', "AAA.").isCompleted() }
        assertFalse { room('B', "ABBD").isCompleted() }
        assertTrue { room('D', "DDDD").isCompleted() }
    }

    @Test
    fun `test room accept`() {
        assertFalse { room('A', "ABCD").accept('A') }
        assertTrue { room('A', "..AA").accept('A') }
        assertFalse { room('B', "..AA").accept('A') }
        assertFalse { room('B', "BBBB").accept('B') }
    }

    @Test
    fun `test room moveablePod`() {
        val pod = room('B', "..BA").moveablePod()
        assertNotNull(pod)
        pod.let {
            assertEquals(Day23.Coord(4, 3), it.position)
            assertEquals('B', it.type)
        }
    }

    @Test
    fun `hallway path free`() {
        val hallway = hallway(".....A.B.C.")
        assertTrue { hallway.isPathFree(0, 2) }
        assertTrue { hallway.isPathFree(4, 2) }

        assertFalse { hallway.isPathFree(9, 6) }
    }

    @Test
    fun `test hallway freePositionsFromRoom`() {
        val hallway = hallway("AA.D.....C.")
        val freePositions0 = hallway.freePositionsFromRoom(Day23.Coord(ROOM_POSITION['A']!!, 0))
        assertEquals(0, freePositions0.size)
        val freePositions1 = hallway.freePositionsFromRoom(Day23.Coord(ROOM_POSITION['B']!!, 0))
        assertEquals(2, freePositions1.size)
        assertEquals(Day23.Coord(5, 0), freePositions1.first())
        assertEquals(Day23.Coord(7, 0), freePositions1.last())
        val freePositions2 = hallway.freePositionsFromRoom(Day23.Coord(ROOM_POSITION['C']!!, 0))
        assertEquals(2, freePositions2.size)
        assertEquals(Day23.Coord(5, 0), freePositions2.first())
        assertEquals(Day23.Coord(7, 0), freePositions2.last())
    }

    @Test
    fun `test hallway moveablePods`() {
        val pods = hallway("AB.C.C....D").moveablePods()
        assertEquals(4, pods.size)
        assertEquals(1, pods[0].position.x)
        assertEquals('B', pods[0].type)
        assertEquals(3, pods[1].position.x)
        assertEquals('C', pods[1].type)
        assertEquals(5, pods[2].position.x)
        assertEquals('C', pods[2].type)
        assertEquals(10, pods[3].position.x)
        assertEquals('D', pods[3].type)
    }

    @Test
    fun `test gameState equality`() {
        val gameState = Day23(sample).parseInputData()
        val gameState2 = Day23(sample).parseInputData()
        assertEquals(gameState, gameState2)
    }

    fun room(type: Char, content: String) = Day23.Room(type, ArrayList(content.toList()))
    fun hallway(content: String) = Day23.Hallway(ArrayList(content.toList()))
}
