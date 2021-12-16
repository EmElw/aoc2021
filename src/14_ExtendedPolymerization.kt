import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private typealias Bigram = Pair<Char, Char>

private fun solve(input: List<String>, steps: Int): Long {
    val base = input.first()
    val rules = input.takeLastWhile { it.isNotBlank() }.associate {
        it.filter { it.isLetter() }.toList().let { (left, right, middle) ->
            // map to list of resulting pairs
            (left to right) to listOf((left to middle), (middle to right))
        }
    }

    val pairs = base.zipWithNext()
    val initialMap = pairs.associateWith { pair -> pairs.count { it == pair }.toLong() }

    val countPairs = (1..steps).fold(initialMap) { map, _ -> map mutateWith rules }

    // count the 2nd letter of each pair so each letter is only counted once but also add the base's first letter
    val countLetters = mutableMapOf<Char, Long>(base.first() to 1)
    countPairs.forEach { (pair, count) -> countLetters.merge(pair.second, count, Long::plus) }

    return countLetters.maxOf { it.value } - countLetters.minOf { it.value }
}

private infix fun Map<Bigram, Long>.mutateWith(rules: Map<Bigram, List<Bigram>>) = let { old ->
    mutableMapOf<Bigram, Long>().also { new ->
        old.forEach { (rule, count) -> rules[rule]!!.forEach { pair -> new.merge(pair, count, Long::plus) } }
    }
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