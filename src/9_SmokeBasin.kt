import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private val ADJ = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

private fun Pair<Int, Int>.adjacent() = let { (x, y) -> ADJ.map { (xx, yy) -> xx + x to yy + y } }

private fun List<String>.toPointMap() =
    flatMapIndexed { y, r -> r.mapIndexed { x, t -> (x to y) to t.digitToInt() } }.associate { it }

fun lowPoints(mat: Map<Pair<Int, Int>, Int>) = mat.filter { (point, height) ->
    point.adjacent().all { adj -> height < mat.getOrDefault(adj, 9) }
}

fun basins(mat: Map<Pair<Int, Int>, Int>) = lowPoints(mat).let { lows ->
    lows.map { (root, _) ->
        val inBasin = mutableSetOf(root)
        val frontier = mutableListOf(root)

        while (frontier.isNotEmpty()) {
            val point = frontier.removeFirst()
            val temp = mat[point]!!
            inBasin.add(point)
            point.adjacent().forEach { (xx, yy) ->
                val t = mat[xx to yy] ?: 9
                if (t != 9 && t > temp) frontier.add(xx to yy)
            }
        }
        inBasin
    }
}

fun basinProduct(input: Map<Pair<Int, Int>, Int>) =
    basins(input).map { it.size }.sortedDescending().take(3).reduce { l, r -> l * r }

internal class SmokeBasin {

    private val sample = File("input/9/sample").readLines().toPointMap()
    private val input = File("input/9/input").readLines().toPointMap()

    @Test
    fun partOne() {
        assertEquals(15, lowPoints(sample).values.sumOf { it + 1 })
        assertEquals(607, lowPoints(input).values.sumOf { it + 1 })
    }

    @Test
    fun partTwo() {
        assertEquals(1134, basinProduct(sample))
        assertEquals(900864, basinProduct(input))
    }
}