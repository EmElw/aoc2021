import java.io.File

fun main() {
    val input = File("input/1").readLines().map { it.toInt() }

    val partOne = input.windowed(2).count { it[1] > it[0] } // expected 7 for test input
    val partTwo = input.windowed(4).count { it[3] > it[0] } // expected 5 for test input

    println("part one: $partOne")
    println("part two: $partTwo")
}