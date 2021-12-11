import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

typealias Point = Pair<Int, Int>
typealias Matrix = MutableMap<Point, Int>

val adjacent = (-1..1).flatMap { x -> (-1..1).map { y -> x to y } }.filterNot { (x, y) -> x == 0 && y == 0 }
private fun Point.adjacent() = let { (x, y) -> adjacent.map { (xx, yy) -> x + xx to y + yy } }

private fun List<String>.toPointMap() =
    flatMapIndexed { y, r -> r.mapIndexed { x, t -> (x to y) to t.digitToInt() } }.associate { it }

fun simOctopus(input: List<String>, steps: Int? = null): Pair<Int?, Matrix> {
    val octopuses = input.toPointMap().toMutableMap()
    var flashCount = 0
    for (step in 1..(steps ?: Int.MAX_VALUE)) {
        // step 1 increment all
        octopuses.mapValuesTo(octopuses) { (_, v) -> v + 1 }

        // step 2 flash all energy >= 9
        val flashing = octopuses.filter { (_, energy) -> energy > 9 }.keys.toMutableList()
        val hasFlashed = mutableSetOf<Point>()
        while (flashing.isNotEmpty()) {
            val p = flashing.removeLast()

            if (!hasFlashed.contains(p)) {
                hasFlashed.add(p)
                flashCount++

                // increment adjacent energy
                p.adjacent().forEach { adj ->
                    octopuses.computeIfPresent(adj) { _, energy -> energy + 1 }.also {
                        if ((it ?: 0) > 9 && !hasFlashed.contains(adj)) flashing.add(adj)
                    }
                }
            }
        }

        // step 3 reset any that has flashed
        hasFlashed.forEach { p -> octopuses[p] = 0 }

        if (hasFlashed.size == 100 && steps == null) return step to octopuses
    }

    return flashCount to octopuses
}

internal class DumboOctopuses {

    private val sample = File("input/11/sample").readLines()
    private val sample1 = File("input/11/sample_step1").readLines()
    private val sample2 = File("input/11/sample_step2").readLines()
    private val input = File("input/11/input").readLines()

    @Test
    fun partOne() {
        assertEquals(sample.toPointMap(), simOctopus(sample, 0).second)
        assertEquals(sample1.toPointMap(), simOctopus(sample, 1).second)
        assertEquals(sample2.toPointMap(), simOctopus(sample, 2).second)
        assertEquals(1656, simOctopus(sample, 100).first)
        assertEquals(1697, simOctopus(input, 100).first)
    }

    @Test
    fun partTwo() {
        assertEquals(195, simOctopus(sample).first)
        assertEquals(344, simOctopus(input).first)
    }
}