import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun List<String>.toProblem() = let { list ->
    list.takeWhile { it.isNotBlank() }.map {
        it.split(",").let { (x, y) -> x.toInt() to y.toInt() }
    }.toSet() to list.takeLastWhile { it.isNotBlank() }.map {
        it.split(" ", "=").takeLast(2).let { (c, v) -> c to v.toInt() }
    }
}

private fun Pair<Set<Pair<Int, Int>>, List<Pair<String, Int>>>.doFolds() = let { (paper, instr) ->
    instr.fold(paper) { dots, (dir, line) ->
        when (dir) {
            "y" -> dots.map { (x, y) -> x to (if (y > line) 2 * line - y else y) }
            "x" -> dots.map { (x, y) -> (if (x > line) 2 * line - x else x) to y }
            else -> error("bad instruction $dir=$line")
        }.toSet()
    }
}

private val sample = File("input/13/sample").readLines()
private val input = File("input/13/input").readLines()

internal class TransparentOrigami {

    @Test
    fun partOne() {
        assertEquals(17, sample.toProblem().let { it.first to it.second.take(1) }.doFolds().size)
        assertEquals(850, input.toProblem().let { it.first to it.second.take(1) }.doFolds().size)
    }

    @Test
    fun partTwo() {
        val image = input.toProblem().doFolds()
        val w = image.maxOf { it.first }
        val h = image.maxOf { it.second }

        println("expect AHGCPGAU")
        for (y in 0..h) {
            for (x in 0..w) print(if (image.contains(x to y)) "█" else " ")
            println()
        }
    }
}
