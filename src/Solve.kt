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

fun <T, V> Iterable<T>.zipPadded(o: Iterable<V>): List<Pair<T, V>> {
    val l = iterator()
    val r = o.iterator()
    val list = mutableListOf<Pair<T, V>>()
    var lVal = l.next()
    var rVal = r.next()

    list.add(lVal to rVal)
    while (l.hasNext() || r.hasNext()) {
        if (l.hasNext()) lVal = l.next()
        if (r.hasNext()) rVal = r.next()
        list.add(lVal to rVal)
    }

    return list
}

infix fun Int.toward(x2: Int) =
    if (this < x2) this..x2 else this downTo x2
