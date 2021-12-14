import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private typealias Bigram = Pair<Char, Char>

fun mutate(map: Map<Bigram, Long>, rules: Map<Bigram, List<Bigram>>) = mutableMapOf<Bigram, Long>().also { newMap ->
    map.forEach { (rule, count) -> rules[rule]!!.forEach { pair -> newMap.compute(pair) { _, v -> (v ?: 0) + count } } }
}

private fun solve(input: List<String>, steps: Int): Long {
    val inputPolymer = input.first()
    val rules = input.takeLastWhile { it.isNotBlank() }.associate {
        val arr = it.toCharArray()
        val left = arr[0]
        val mid = arr.last()
        val right = arr[1]
        // map to list of resulting pairs
        (left to right) to listOf((left to mid), (mid to right))
    }

    val pairs = inputPolymer.zipWithNext()
    val initialMap = pairs.associateWith { pairs.count { p -> it == p }.toLong() }

    val countPairs = (1..steps).fold(initialMap) { map, _ -> mutate(map, rules) }

    // count the 2nd letter of each pair so each letter is only counted once but also add the first input letter
    val countLetters = mutableMapOf<Char, Long>(inputPolymer.first() to 1)
    countPairs.forEach { (pair, count) -> countLetters.compute(pair.second) { _, v -> (v ?: 0) + count } }

    return countLetters.maxOf { it.value } - countLetters.minOf { it.value }
}

internal class ExtendedPolymerization {

    val sample = File("input/14/sample").readLines()
    val input = File("input/14/input").readLines()

    @Test
    fun partOne() {
        assertEquals(1588, solve(sample, 10))
        assertEquals(2891, solve(input, 10))
    }

    @Test
    fun partTwo() {
        assertEquals(2188189693529, solve(sample, 40))
        assertEquals(4607749009683, solve(input, 40))
    }
}