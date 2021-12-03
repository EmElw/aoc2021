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
    return gamma.joinToString("").toInt(2) *
            gamma.map { 1 - it }.joinToString("").toInt(2)
}

fun calcLifeSupport(input: List<List<Int>>): Int {
    var index = 0
    var oxygen = input
    var co2 = input

    while (oxygen.size > 1 || co2.size > 1) {
        if (oxygen.size > 1)
            oxygen = oxygen.filter { it[index] == oxygen.mostCommon(index) }
        if (co2.size > 1)
            co2 = co2.filter { it[index] == co2.leastCommon(index) }

        index++
    }

    return oxygen.first().joinToString("").toInt(2) *
            co2.first().joinToString("").toInt(2)
}

private fun List<List<Int>>.mostCommon(index: Int): Int {
    val zeroes = this.count { it[index] == 0 }
    val ones = this.size - zeroes

    return when {
        zeroes > ones -> 0
        else -> 1
    }
}

private fun List<List<Int>>.leastCommon(index: Int): Int {
    val zeroes = this.count { it[index] == 0 }
    val ones = this.size - zeroes
    return when {
        ones < zeroes -> 1
        else -> 0
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