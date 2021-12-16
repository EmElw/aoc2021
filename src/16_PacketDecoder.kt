import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class PacketDecoder {

    val input = File("input/16/input").readLines()

    @Test
    fun partOne() {
        sumVer = 0
        packetFromString(input.first())
        assertEquals(854, sumVer)
    }

    @Test // sum of 1 and 2
    fun sum() = assertEquals(3, packetFromString("C200B40A82"))

    @Test
    fun prod() = assertEquals(54, packetFromString("04005AC33890"))

    @Test
    fun min() = assertEquals(7, packetFromString("880086C3E88112"))

    @Test
    fun max() = assertEquals(9, packetFromString("CE00C43D881120"))

    @Test
    fun fiveLessThanFifteen() = assertEquals(1, packetFromString("D8005AC2A8F0"))

    @Test
    fun fiveNotGreaterThanFifteen() = assertEquals(0, packetFromString("F600BC2D8F"))

    @Test
    fun fiveNotEqualFifteen() = assertEquals(0, packetFromString("9C005AC2F8F0"))

    @Test
    fun arithmetic() = assertEquals(1, packetFromString("9C0141080250320F1802104A08"))

    @Test
    fun partTwo() = assertEquals(186189840660, packetFromString(input.first()))
}

private val mask = List(4) { (8).shr(it) }

private fun packetFromString(input: String) = input.flatMap { hex ->
    hex.digitToInt(16).let { mask.map { bit -> it.and(bit).coerceAtMost(1) } }
}.toMutableList().let { packetFrom(it) }

private fun MutableList<Int>.readBits(n: Int) = (1..n).map { this.removeFirst() }
private fun MutableList<Int>.readAsNumber(n: Int) = readBits(n).joinToString("").toInt(2)
private fun MutableList<Int>.asNumber() = joinToString("").toLong(2)

private fun <E, T> MutableList<E>.drain(f: (MutableList<E>) -> T): List<T> {
    val acc = mutableListOf<T>()
    while (this.isNotEmpty()) acc += f(this)
    return acc
}

private var sumVer = 0
private fun packetFrom(queue: MutableList<Int>): Long = run { sumVer += queue.readAsNumber(3) }.let {
    when (val type = queue.readAsNumber(3)) {
        4 -> {
            var notLast = queue.removeFirst()
            val acc = queue.readBits(4).toMutableList()
            while (notLast == 1) {
                notLast = queue.removeFirst()
                acc += queue.readBits(4)
            }
            acc.asNumber()
        }
        else -> {
            when (val lenType = queue.removeFirst()) {
                0 -> queue.readBits(queue.readAsNumber(15)).toMutableList().drain { packetFrom(it) }
                1 -> (1..queue.readAsNumber(11)).map { packetFrom(queue) }
                else -> error("bad len type $lenType")
            }.let { ops ->
                when (type) {
                    0 -> ops.sum()
                    1 -> ops.fold(1) { acc, n -> acc * n }
                    2 -> ops.minOf { it }
                    3 -> ops.maxOf { it }
                    5 -> ops.let { (a, b) -> if (a > b) 1 else 0 }
                    6 -> ops.let { (a, b) -> if (a < b) 1 else 0 }
                    7 -> ops.let { (a, b) -> if (a == b) 1 else 0 }
                    else -> error("bad type $type")
                }
            }
        }
    }
}