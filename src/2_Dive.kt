import java.io.File

fun simpleSubMovement(input: List<String>): Pair<Int, Int> {
    var depth = 0
    var x = 0

    input.forEach {
        val (op, _mag) = it.split(" ")
        val mag = _mag.toInt()
        when (op) {
            "forward" -> x += mag
            "up" -> depth -= mag
            "down" -> depth += mag
        }
    }
    return Pair(depth, x)
}

fun complexSubMovement(input: List<String>): Pair<Int, Int> {
    var depth = 0
    var x = 0
    var aim = 0

    input.forEach {
        val (op, _operand) = it.split(" ")
        val operand = _operand.toInt()
        when (op) {
            "forward" -> {
                depth += aim * operand
                x += operand
            }
            "up" -> aim -= operand
            "down" -> aim += operand
        }
    }
    return Pair(depth, x)
}


data class Vec(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
    operator fun plus(o: Vec): Vec {
        return Vec(this.x + o.x, this.y + o.y, this.z + o.z)
    }
}


fun main() {
    val input = File("input/input").readLines()

    val (depth, x) = simpleSubMovement(input)
    println("part one: ${depth * x}") // expected 150 for sample input

    val f = { acc: Vec, (op: String, o: String): List<String> ->
        val operand = o.toInt()
        when (op) {
            "forward" -> acc + Vec(x = operand)
            "up" -> acc + Vec(y = -operand)
            "down" -> acc + Vec(y = operand)
            else -> acc
        }
    }
    val altOne = input.map { it.split(" ") }.fold(Vec(), f).run { this.x * this.y }
    println("alternative one: $altOne")

    val (depthComplex, xComplex) = complexSubMovement(input)
    println("part two: ${depthComplex * xComplex}") // expected 900 for sample input

    val altTwo = input.map { it.split(" ") }.fold(Vec()) { acc, (op, o) ->
        val operand = o.toInt()
        when (op) {
            "forward" -> acc + Vec(x = operand, y = acc.z * operand)
            "up" -> acc + Vec(z = -operand)
            "down" -> acc + Vec(z = operand)
            else -> acc
        }
    }.run { this.x * this.y }
    println("alternative two: $altTwo")
}

