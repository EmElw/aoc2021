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

fun main() {
    val input = File("test/4").readLines()
    val (boards, order) = parseBingo(input)
    val partOne = play(boards, order)
    val partTwo = playToLose(boards, order)
    println("part one: $partOne ") //expected 4512 for test input
    println("part two: $partTwo") //expected 1924 for test input
}