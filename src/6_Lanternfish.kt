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

// alt approach sim each fish since they don't interact with each other, with some memoization on top
val mem = mutableMapOf<Pair<Int, Int>, Long>()
fun fish_(days: Int, life: Int): Long = mem[days to life] ?: when {
    days == 0 -> 1
    life == 0 -> fish_(days - 1, 6) + fish_(days - 1, 8)
    else -> fish_(days - 1, life - 1)
}.also { mem[days to life] = it }

internal class LanternFish {

    private val inputFish = File("input/6/input").readLines().first().split(",").map { it.toInt() }

    @Test
    fun fish() {
        assertEquals(fish(inputFish, 256).sum(), 1710166656900)
    }

    @Test
    fun fish_() {
        assertEquals(inputFish.sumOf { life -> fish_(256, life) }, 1710166656900)
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