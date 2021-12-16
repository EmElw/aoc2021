import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals


private val adjacent = (-1..1).flatMap { x -> (-1..1).map { y -> x to y } }
    .filterNot { (x, y) -> (x == 0 && y == 0) || (x != 0 && y != 0) }

private fun Point.adjacent() = let { (x, y) -> adjacent.map { (xx, yy) -> x + xx to y + yy } }


private fun List<String>.toPointMap() = flatMapIndexed { y, line ->
    line.mapIndexed { x, c -> (x to y) to c.digitToInt() }
}.associate { it }

private fun Point.inBounds(X: Int, Y: Int, minX: Int = 0, minY: Int = 0) = let { (x, y) ->
    x in minX..X && y in minY..Y
}

private fun findLeastDangerous(danger: Map<Point, Int>): Int {

    val start = 0 to 0

    val dist = mutableMapOf<Point, Int>()
    val prev = mutableMapOf<Point, Point?>()
    val unexplored = danger.keys.toMutableSet()
    for (v in unexplored) {
        dist[v] = Int.MAX_VALUE
        prev[v] = null
    }

    val comp = { a: Point, b: Point -> (dist[a] ?: Int.MAX_VALUE) - (dist[b] ?: Int.MAX_VALUE) }

    while (unexplored.isNotEmpty()) {
        val u = unexplored.minByOrNull { dist[it]!! }!!
        unexplored.remove(u)

        u.adjacent().filter { it in unexplored }.forEach { v ->
            val alt = dist[u]!! + danger[v]!!
            if (alt < (dist[v] ?: Int.MAX_VALUE)) {
                dist[v] = alt
                prev[v] = u
            }
        }
    }

    return dist.maxByOrNull { (k, _) -> k.first + k.second }!!.value
}

internal class Chiton {

    val sample = File("input/15/sample").readLines()
    val input = File("input/15/input").readLines()

    @Test
    fun partOne() {
        assertEquals(40, findLeastDangerous(sample.toPointMap()))
        assertEquals(388, findLeastDangerous(input.toPointMap()))
    }

    @Test
    fun partTwo() {
        assertEquals(315, findLeastDangerous(sample.toPointMap().supersize()))
        assertEquals(40, findLeastDangerous(input.toPointMap().supersize()))
    }

}

private fun Map<Point, Int>.supersize(): Map<Point, Int> = let { oldMap ->
    val w = oldMap.maxOf { it.key.first } + 1
    mutableMapOf<Point, Int>().also { newMap ->
        oldMap.forEach { (x, y), danger ->
            for (xx in 0..4) {
                for (yy in 0..4) {
                    newMap[x + w * xx to y + w * yy] = (danger + xx + yy).let { if (it > 9) it - 9 else it }
                }
            }
        }
    }
}
