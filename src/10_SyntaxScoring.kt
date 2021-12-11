import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

val opening = listOf('(', '[', '{', '<')
val closing = listOf(')', ']', '}', '>')
val parens = (opening zip closing).toMap()


enum class LineType {
    INCOMPLETE, CORRUPTED,
}

fun String.processLine(): Pair<LineType, String> {
    val stack = mutableListOf<Char>()
    for (c in this) {
        when (c) {
            in opening -> stack.add(c)
            in closing -> if (parens[stack.removeLast()] == c) continue
            else return LineType.CORRUPTED to c.toString()
        }
    }
    return LineType.INCOMPLETE to stack.map { parens[it] }.reversed().joinToString("")
}

internal class SyntaxScoring {

    private val sample = File("input/10/sample").readLines()
    private val sampleCorrupted = File("input/10/sample_corrupted").readLines()
    private val input = File("input/10/input").readLines()

    private val errorScore: (List<String>) -> Int = {
        it.map { it.processLine() }.filter { it.first == LineType.CORRUPTED }.sumOf { (_, paren) ->
            when (paren) {
                ")" -> 3
                "]" -> 57
                "}" -> 1197
                ">" -> 25137
                else -> error("")
            }.toInt()
        }
    }

    private val completionScore: (List<String>) -> Long = {
        it.map { it.processLine() }.filter { it.first == LineType.INCOMPLETE }.map { (_, completion) ->
            completion.fold((0).toLong()) { score, paren ->
                (score * 5) + when (paren) {
                    ')' -> 1
                    ']' -> 2
                    '}' -> 3
                    '>' -> 4
                    else -> error("")
                }
            }
        }.sorted().let { it[it.size / 2] }
    }

    @Test
    fun partOne() {
        assertEquals(1197, errorScore(sampleCorrupted))
        assertEquals(26397, errorScore(sample))
        assertEquals(299793, errorScore(input))
    }

    @Test
    fun partTwo() {
        assertEquals("}}]])})]", ("[({(<(())[]>[[{[]{<()<>>").processLine().second)
        assertEquals(288957, completionScore(sample))
        assertEquals(3654963618, completionScore(input))
    }
}