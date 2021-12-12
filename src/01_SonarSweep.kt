import org.junit.jupiter.api.Test
import java.io.File

private fun countIncreases(input: List<String>): Int = input.map { it.toInt() }.windowed(2).count { it[1] > it[0] }
private fun countSlidingIncreases(input: List<String>): Int =
    input.map { it.toInt() }.windowed(4).count { it[3] > it[0] }

internal class SonarSweep {
    @Test
    fun partOne() {
        File("input/01/sample").readLines().solve(
            ::countIncreases to 7,
        )
        File("input/01/input").readLines().solve(
            ::countIncreases to 1292,
        )
    }

    @Test
    fun partTwo() {
        File("input/01/sample").readLines().solve(
            ::countSlidingIncreases to 5,
        )
        File("input/01/input").readLines().solve(
            ::countSlidingIncreases to 1262,
        )
    }
}