import org.junit.jupiter.api.Test
import java.io.File

typealias Board = List<List<Int>>
typealias Boards = List<Board>
typealias Order = List<Int>

const val CLEAR = -1

fun parseBingo(input: List<String>): Pair<Boards, Order> {
    val order = input.first().split(",").map { it.toInt() }
    order.indices
    val boards = input.drop(2).filter { it.isNotBlank() }.chunked(5).map { block ->
        block.map { row ->
            row.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        }
    }

    return Pair(boards, order)
}

fun checkBoard(board: Board): Boolean = (board.any { row -> row.all { it == CLEAR } }) ||     // rows
        (0..board.lastIndex).any { col -> (board.all { it[col] == CLEAR }) }            // columns

fun play(boards: Boards, order: Order): Int {
    order.fold(boards) { state, draw ->
        state.map { board -> board.map { row -> row.map { num -> if (num == draw) CLEAR else num } } }
            .also { newState ->
                val winningBoard = newState.find(::checkBoard)
                if (winningBoard != null) return winningBoard.sumOf { it.filter { num -> num != CLEAR }.sum() } * draw
            }
    }

    error("no completed board")
}

fun altPlay(boards: Boards, order: Order, prevDraw: Int? = null): Int = when (boards.firstOrNull(::checkBoard)) {
    null -> altPlay(
        boards.map { board -> board.map { row -> row.map { num -> if (num == order.first()) CLEAR else num } } },
        order.drop(1),
        order.first()
    )
    else -> boards.find(::checkBoard)!!.sumOf { row -> row.filter { num -> num != CLEAR }.sum() } * prevDraw!!
}

fun playToLose(boards: Boards, order: Order): Int {
    order.fold(boards) { state, draw ->
        state.map { board -> board.map { row -> row.map { num -> if (num == draw) CLEAR else num } } }
            .filterNot(::checkBoard).also { newState ->
                if (newState.isEmpty()) {
                    val sum = state.first().sumOf { it.filter { num -> num != CLEAR && num != draw }.sum() }
                    return sum * draw
                }
            }
    }

    error("no completed board")
}

private fun countIncreases(input: List<String>): Int {
    val (boards, draw) = parseBingo(input)
    return play(boards, draw)
}

private fun countSlidingIncreases(input: List<String>): Int {
    val (boards, draw) = parseBingo(input)
    return playToLose(boards, draw)
}

internal class GiantSquid {
    @Test
    fun partOne() {
        File("input/04/sample").readLines().solve(
            ::countIncreases to 4512,
        )
        File("input/04/input").readLines().solve(
            ::countIncreases to 39984,
        )
    }

    @Test
    fun partTwo() {
        File("input/04/sample").readLines().solve(
            ::countSlidingIncreases to 1924,
        )
        File("input/04/input").readLines().solve(
            ::countSlidingIncreases to 8468,
        )
    }
}