package mri.advent.y2021


enum class Direction(val x: Int, val y: Int) {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0),
    UPRIGHT(1, -1), DOWNRIGHT(1, 1), DOWNLEFT(-1, 1), UPLEFT(-1, -1);
}

data class Point(val x: Int, val y: Int) {
    fun to(direction: Direction) = Point(x + direction.x, y + direction.y)
    override fun toString() = "[$x,$y]"
    companion object {
        fun parse(string: String): Point {
            val parts = string.split(",")
            return Point(parts[0].toInt(), parts[1].toInt())
        }
    }
}

