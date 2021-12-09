import org.junit.jupiter.api.Test
import java.io.File

typealias Matrix = List<List<Int>>

val adjacent = listOf(
    1 to 0,
    -1 to 0,
    0 to 1,
    0 to -1,
)

private fun Pair<Int, Int>.isInBounds(matrix: Matrix): Boolean =
    (this.first in 0..matrix.first().lastIndex && this.second in 0..matrix.lastIndex)

fun findLowPoints(input: List<String>): Matrix = input.map { line -> line.map { c -> c.digitToInt() } }.let { matrix ->
    matrix.mapIndexed { y, row ->
        row.mapIndexed { x, temp ->
            if (adjacent.all { (xx, yy) ->
                    val ox = x + xx
                    val oy = y + yy
                    if ((ox to oy).isInBounds(matrix)) temp < matrix[oy][ox] else true
                }) temp else -1
        }
    }
}

fun sumLowPoints(input: List<String>): Int = findLowPoints(input).sumOf {
    it.filter { it != -1 }.sumOf { it + 1 }
}

fun findBasins(input: List<String>): Int {
    val matrix = input.map { line -> line.map { c -> c.digitToInt() } }
    val lowPoints = findLowPoints(input)
    val basinSizes = mutableListOf<Int>()

    lowPoints.forEachIndexed { rootX, row ->
        row.forEachIndexed { rootY, rootTemp ->
            if (rootTemp != -1) {
                val basin = mutableSetOf<Pair<Int, Int>>()
                val frontier = mutableListOf<Pair<Int, Int>>()
                frontier.add(rootY to rootX)

                while (frontier.isNotEmpty()) {
                    val (x, y) = frontier.removeLast()
                    basin.add(x to y)
                    val temp = matrix[y][x]
                    for ((xx, yy) in adjacent) {
                        val ox = x + xx
                        val oy = y + yy
                        if ((ox to oy).isInBounds(matrix) && matrix[oy][ox] > temp && matrix[oy][ox] != 9) frontier.add(
                            ox to oy
                        )
                    }
                }
                basinSizes.add(basin.size)
            }
        }
    }

    basinSizes.sortDescending()
    return basinSizes.take(3).reduce { l, r -> l * r }
}


internal class SmokeBasin {

    private val sample = File("input/9/sample").readLines()
    private val input = File("input/9/input").readLines()

    @Test
    fun partOne() {
        sample.solve(::sumLowPoints to 15)
        input.solve(::sumLowPoints to 607)
    }

    @Test
    fun partTwo() {
        sample.solve(::findBasins to 1134)
        input.solve(::findBasins to 900864)
    }
}