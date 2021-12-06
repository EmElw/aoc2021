import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

fun fish(initial: List<Int>, day: Int = 1): List<Long> = when (day) {
    0 -> (0..8).map { initial.count { n -> it == n }.toLong() }
    else -> fish(initial, day - 1).pad(10, 0).toMutableList().also {
        it[7] += it[0]
        it[9] += it[0]
    }.drop(1)
}

internal class LanternFish {

    @Test
    fun fishCycle() {
        assertEquals(
            listOf<Long>(1, 1, 3, 1, 1, 0, 1, 0, 0), fish(listOf(3, 4, 3, 1, 2, 3, 5, 7))
        )
    }

    private fun parse(input: List<String>): List<Int> = input.first().split(",").map { it.toInt() }
    private val solveOne = fun(input: List<String>): Long = fish(parse(input), 80).sum()
    private val solveTwo = fun(input: List<String>): Long = fish(parse(input), 256).sum()

    @Test
    fun partOne() {
        File("input/6/sample").readLines().solve(solveOne to 5934)
        File("input/6/input").readLines().solve(solveOne to 380612)
    }

    @Test
    fun partTwo() {
        File("input/6/sample").readLines().solve(solveTwo to 26984457539)
        File("input/6/input").readLines().solve(solveTwo to 1710166656900)
    }
}