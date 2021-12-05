import kotlin.test.assertEquals

fun List<String>.solve(vararg pairs: Pair<(List<String>) -> Int, Int?>) {
    pairs.forEach { (function, solution) ->
        if (solution == null) {
            println("new result ${function(this)}")
        } else {
            assertEquals(solution, function(this))
        }
    }
}