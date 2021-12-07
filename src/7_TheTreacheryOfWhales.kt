import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.max


// TODO binary search probably works
fun costOf(position: Int, crabs: List<Int>, f: (Int, Int) -> Int): Int = crabs.sumOf { f(position, it) }

fun binarySearchingCrabs(input: List<String>): Int {
    val positions = input.first().split(",").map { it.toInt() }
    val f = fun(crab: Int, pos: Int) = (crab - pos).absoluteValue

    var (min, max) = positions.fold((positions.first() to positions.first())) { (min, max), it ->
        min(min, it) to max(max, it)
    }

    var pivotIndex = (min + max) / 2
    var leftIndex = (min + pivotIndex) / 2
    var rightIndex = (pivotIndex + max) / 2

    while (pivotIndex != leftIndex || pivotIndex != rightIndex) {

        val leftCost = costOf(leftIndex, positions, f)
        val rightCost = costOf(rightIndex, positions, f)

        if (leftCost < rightCost) {
            max = pivotIndex
            pivotIndex = leftIndex
        } else {
            min = pivotIndex
            pivotIndex = rightIndex
        }
        leftIndex = (min + pivotIndex) / 2
        rightIndex = (pivotIndex + max) / 2
    }

    return costOf(pivotIndex, positions, f)

}

fun crabOpt(input: List<String>): Int {
    val positions = input.first().split(",").map { it.toInt() }
    val min = positions.minOf { it }
    val max = positions.maxOf { it }

    return (min..max).map { pos ->
        positions.sumOf { crabPos ->
            (crabPos - pos).absoluteValue
        }
    }.minOf { it }
}

fun crabOptTwo(input: List<String>): Int {
    val positions = input.first().split(",").map { it.toInt() }
    val min = positions.minOf { it }
    val max = positions.maxOf { it }

    return (min..max).map { pos ->
        positions.sumOf { crabPos ->
            val d = (crabPos - pos).absoluteValue
            d * (d + 1) / 2
        }
    }.minOf { it }
}

internal class TheTreacheryOfWhales {

    private val sample = File("input/7/sample").readLines()
    private val input = File("input/7/input").readLines()

    @Test
    fun partOne() {
        sample.solve(::crabOpt to 37)
        input.solve(::crabOpt to 364898)
    }

    @Test
    fun partTwo() {
        sample.solve(::crabOptTwo to 168)
        input.solve(::crabOptTwo to null)
    }

    @Test
    fun binSearchVer() {
        sample.solve(::binarySearchingCrabs to 37)
        input.solve(::binarySearchingCrabs to 364898)
    }
}