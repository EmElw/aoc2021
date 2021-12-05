import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.*


fun countOverlaps(input: List<String>, countDiagonals: Boolean = false): Int {
    val lines = input.map { it.split("->", ",").map { it.trim().toInt() } }.filter { (x1, y1, x2, y2) ->
        countDiagonals || x1 == x2 || y1 == y2
    }

    val matrix = mutableMapOf<Pair<Int, Int>, Int>()

    lines.forEach { (x1, y1, x2, y2) ->
        var x = x1
        var y = y1
        val dx = (x2 - x1).sign
        val dy = (y2 - y1).sign

        while (x != x2 || y != y2) {
            matrix[x to y] = (matrix[x to y] ?: 0) + 1
            x += dx
            y += dy
        }
        matrix[x to y] = (matrix[x to y] ?: 0) + 1
    }

    return matrix.count { it.value >= 2 }
}

fun solveOne(input: List<String>): Int {
    return countOverlaps(input)
}

fun solveTwo(input: List<String>): Int {
    return countOverlaps(input, true)
}


internal class HydrothermalVenture {
    @Test
    fun partOne() {
        File("input/5/sample").readLines().solve(
            ::solveOne to 5,
        )
        File("input/5/input").readLines().solve(
            ::solveOne to 5373,
        )
    }

    @Test
    fun partTwo() {
        File("input/5/sample").readLines().solve(
            ::solveTwo to 12,
        )
        File("input/5/input").readLines().solve(
            ::solveTwo to 21514,
        )
    }
}