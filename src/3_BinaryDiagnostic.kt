import org.junit.jupiter.api.Test
import java.io.File

fun calcPowerConsumption(input: List<String>): Int {
    val m = input.map { line ->
        line.map { it.digitToInt() }
    }
    val height = m.size

    val gamma = m.reduce { acc, it ->
        acc.zip(it).map { (l, r) -> l + r }
    }.map {
        when {
            it > height / 2.0 -> 1
            it < height / 2.0 -> 0
            else -> error("Equal number ones and zeroes")
        }
    }
    return gamma.joinToString("").toInt(2) * gamma.map { 1 - it }.joinToString("").toInt(2)
}

fun calcLifeSupport(input: List<String>): Int {
    val m = input.map { line ->
        line.map { it.digitToInt() }
    }
    var index = 0
    var oxy = m
    var co2 = m

    while (oxy.size > 1 || co2.size > 1) {
        if (oxy.size > 1) {
            val bcOxygen = oxy.bitCriteria(index, 1)
            oxy = oxy.filter { it[index] == bcOxygen }
        }
        if (co2.size > 1) {
            val bcCO2 = co2.bitCriteria(index, 0)
            co2 = co2.filter { it[index] == bcCO2 }
        }
        index++
    }

    return oxy.first().joinToString("").toInt(2) * co2.first().joinToString("").toInt(2)
}

private fun List<List<Int>>.bitCriteria(index: Int, fallback: Int): Int {
    val zeroes = this.count { it[index] == 0 }
    val ones = this.size - zeroes

    // it just works
    return when {
        ones < zeroes -> 1 - fallback
        else -> fallback
    }
}

internal class BinaryDiagnostics {
    @Test
    fun partOne() {
        File("input/3/sample").readLines().solve(
            ::calcPowerConsumption to 198,
        )
        File("input/3/input").readLines().solve(
            ::calcPowerConsumption to 3847100,
        )
    }

    @Test
    fun partTwo() {
        File("input/3/sample").readLines().solve(
            ::calcLifeSupport to 230,
        )
        File("input/3/input").readLines().solve(
            ::calcLifeSupport to 4105235,
        )
    }
}













