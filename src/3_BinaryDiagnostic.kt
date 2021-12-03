import java.io.File

fun calcPowerConsumption(input: List<List<Int>>): Int {
    val height = input.size

    val gamma = input.reduce { acc, it ->
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

fun calcLifeSupport(input: List<List<Int>>): Int {
    var index = 0
    var oxy = input
    var co2 = input

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

    /**                 fb 0         fb 1
     *       more 1      0            1
     *       less 1      1            0
     *       equal       0            1
     */

    return when {
        ones < zeroes -> 1 - fallback
        else -> fallback
    }
}

fun main() {
    val input = File("test/3").readLines().map { str ->
        str.map { it.digitToInt() } // convert to a matrix
    }

    val partOne = calcPowerConsumption(input)
    println("part one: $partOne") // expected 198 for test input

    val partTwo = calcLifeSupport(input)
    println("part two: $partTwo") // expected 230 for test input
}













