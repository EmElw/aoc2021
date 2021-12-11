import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

val opening = listOf('(', '[', '{', '<')
val closing = listOf(')', ']', '}', '>')
val parens = (opening zip closing).toMap()

fun isCorrupted(line: String): Pair<Boolean, Char?> {
    val openStack = mutableListOf<Char>()
    for (current in line) {
        if (current in opening) {
            openStack.add(current)
        } else {
            val lastOpen = openStack.removeLast()
            if (parens[lastOpen]!! != current) return true to current
        }
    }
    return false to null
}

fun scoreCorrupted(input: List<String>) = input.sumOf { line ->
    val (corrupt, char) = isCorrupted(line)
    if (corrupt) when (char) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> error("")
    }
    else 0 as Int
}

fun completeLine(line: String): String {
    val openStack = mutableListOf<Char>()
    for (current in line) {
        if (current in opening) {
            openStack.add(current)
        } else {
            openStack.removeLast()
        }
    }
    return openStack.map { parens[it]!! }.reversed().joinToString("")
}

fun scoreCompletions(input: List<String>) = input.filterNot { isCorrupted(it).first }.map {
    completeLine(it).fold((0).toLong()) { score, char ->
        (score * 5) + when (char) {
            ')' -> 1
            ']' -> 2
            '}' -> 3
            '>' -> 4
            else -> error("")
        }
    }
}.sorted().let { it[it.size / 2] }

// recreational recursion
fun isCorruptedRec(line: String, openStack: String = ""): Pair<Boolean, Char?> =
    if (line.isEmpty()) false to null else line.first().let { cur ->
        when {
            cur in opening -> isCorruptedRec(line.drop(1), openStack + cur)
            cur in closing && parens[openStack.last()] == cur -> isCorruptedRec(line.drop(1), openStack.dropLast(1))
            else -> true to cur
        }
    }

fun completeLineRec(line: String, openStack: String = ""): String = when (line.isEmpty()) {
    true -> openStack.map { parens[it]!! }.reversed().joinToString("")
    else -> line.first().let { curr ->
        when (curr) {
            in opening -> completeLineRec(line.drop(1), openStack + curr)
            else -> completeLineRec(line.drop(1), openStack.dropLast(1))
        }
    }
}

internal class SyntaxScoring {

    private val sample = File("input/10/sample").readLines()
    private val sampleCorrupted = File("input/10/sample_corrupted").readLines()
    private val input = File("input/10/input").readLines()

    @Test
    fun partOne() {
        assertEquals(1197, scoreCorrupted(sampleCorrupted))
        assertEquals(26397, scoreCorrupted(sample))
        assertEquals(299793, scoreCorrupted(input))
    }

    @Test
    fun partTwo() {
        assertEquals("}}]])})]", completeLine("[({(<(())[]>[[{[]{<()<>>"))
        assertEquals(288957, scoreCompletions(sample))
        assertEquals(3654963618, scoreCompletions(input))
    }
}