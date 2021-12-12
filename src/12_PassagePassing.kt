import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

typealias Relations = Map<String, Set<String>>
typealias Path = List<String>

internal class PassagePassing {

    private val sample1 = File("input/12/sample1").readLines().toRelationMap()
    private val sample2 = File("input/12/sample2").readLines().toRelationMap()
    private val sample3 = File("input/12/sample3").readLines().toRelationMap()
    private val input = File("input/12/input").readLines().toRelationMap()

    @Test
    fun partOne() {
        assertEquals(10, findAll(sample1).size)
        assertEquals(19, findAll(sample2).size)
        assertEquals(226, findAll(sample3).size)
        assertEquals(5252, findAll(input).size)
    }

    @Test
    fun partTwo() {
        assertEquals(36, findAll(sample1, dts = 1).toSet().size)
        assertEquals(103, findAll(sample2, dts = 1).toSet().size)
        assertEquals(3509, findAll(sample3, dts = 1).toSet().size)
        assertEquals(147784, findAll(input, dts = 1).toSet().size)
    }
}


private fun List<String>.toRelationMap(): Relations =
    map { it.split("-") }.fold(mutableMapOf<String, MutableSet<String>>()) { map, (a, b) ->
        map.also {
            it.getOrPut(a) { mutableSetOf() }.add(b)
            it.getOrPut(b) { mutableSetOf() }.add(a)
        }
    }


fun findAll(
    rel: Relations, cur: String = "start", path: Path = listOf(), small: List<String> = listOf(), dts: Int = 0
): List<Path> = when (cur) {
    "end" -> listOf(path + cur)
    else -> rel[cur]!!.filterNot { it in small }.flatMap { adj ->
        if (dts > 0 && cur.first().isLowerCase() && cur != "start") {
            findAll(rel, adj, path + cur, small, dts - 1) + findAll(rel, adj, path + cur, small + cur, dts)
        } else {
            findAll(rel, adj, path + cur, if (cur.first().isLowerCase()) small + cur else small, dts)
        }
    }
}

