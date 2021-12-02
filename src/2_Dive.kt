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


fun main() {
    val input = File("input/2").readLines()

    val (depth, x) = simpleSubMovement(input)
    println("part one: ${depth * x}") // expected 150 for test input

    val (depthComplex, xComplex) = complexSubMovement(input)
    println("part two: ${depthComplex * xComplex}") // expected 900 for test input
}