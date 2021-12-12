import org.junit.jupiter.api.Test
import java.io.File

fun simpleSubMovement(input: List<String>): Int =
    input.map { it.split(" ").let { (op, mag) -> op to mag.toInt() } }.map { (op, X) ->
        when (op) {
            "forward" -> Vector(X)
            "up" -> Vector(0, -X)
            "down" -> Vector(0, X)
            else -> error("unrecognized instruction")
        }
    }.reduce { a, b -> a + b }.let { it.x * it.y }


fun complexSubMovement(input: List<String>): Int {
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

    return depth * x
}

internal class Dive {

    @Test
    fun partOne() {
        File("input/02/sample").readLines().solve(
            ::simpleSubMovement to 150
        )
        File("input/02/input").readLines().solve(
            ::simpleSubMovement to 2272262
        )
    }

    @Test
    fun partTwo() {
        File("input/02/sample").readLines().solve(
            ::complexSubMovement to 900
        )
        File("input/02/input").readLines().solve(
            ::complexSubMovement to 2134882034
        )
    }
}

