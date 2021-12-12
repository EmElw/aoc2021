import org.junit.jupiter.api.Test
import java.io.File

fun countEasyDigits(input: List<String>): Int = input.sumOf { line ->
    line.split("|")[1].trim().split(" ").count {
        it.trim().length in listOf(2, 3, 4, 7)
    }
}

fun decode(input: List<String>): Int =
    input.sumOf { line ->
        val (digits, output) = line.split("|").map { it.trim().split(" ").map { it.trim() } }
        val lengths = digits.associate { it.length to it.toSet() }
        output.map { decodeDigit(lengths, it.toSet()) }.joinToString("").toInt()
    }

fun decodeDigit(lengths: Map<Int, Set<Char>>, digit: Set<Char>): Char {
    val size = digit.size
    val one = (lengths[2]!! intersect digit).size // overlap with the '1' set
    val four = (lengths[4]!! intersect digit).size // overlap with the '4' set

    return when {
        size == 2 -> '1'
        size == 3 -> '7'
        size == 4 -> '4'
        size == 5 && one == 2 -> '3'
        size == 5 && four == 3 -> '5'
        size == 5 -> '2'
        size == 6 && four == 4 -> '9'
        size == 6 && one == 2 -> '0'
        size == 6 -> '6'
        size == 7 -> '8'
        else -> error("")
    }
}

internal class SevenSegmentSearch() {
    private val sample = File("input/08/sample").readLines()
    private val input = File("input/08/input").readLines()

    @Test
    fun partOne() {
        sample.solve(::countEasyDigits to 26)
        input.solve(::countEasyDigits to 303)
    }

    @Test
    fun partTwo() {
        sample.solve(::decode to 61229)
        input.solve(::decode to 961734)

    }
}