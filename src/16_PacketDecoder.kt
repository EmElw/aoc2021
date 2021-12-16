import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class PacketDecoder {

    val samples = File("input/16/sample").readLines()
    val input = File("input/16/input").readLines()

    @Test
    fun partOne() {
        val (l0, l1, l2, l3, ex) = samples
        val ex2 = samples[5]
        val exPack2 = toPacket(ex2).first
        val exPack = toPacket(ex).first
        assertEquals(2021, exPack.value)
        assertEquals(6, exPack.sumVersion())
        assertEquals(1, exPack2.version)

        assertEquals(16, toPacket(l0).first.sumVersion())
        assertEquals(12, toPacket(l1).first.sumVersion())
        assertEquals(23, toPacket(l2).first.sumVersion())
        assertEquals(31, toPacket(l3).first.sumVersion())


        val inputPacket = toPacket(input.first()).first
        assertEquals(854, inputPacket.sumVersion())
    }

    @Test
    fun sum() = assertEquals(3, toPacket("C200B40A82").first.value)

    @Test
    fun prod() = assertEquals(54, toPacket("04005AC33890").first.value)

    @Test
    fun min() = assertEquals(7, toPacket("880086C3E88112").first.value)

    @Test
    fun max() = assertEquals(9, toPacket("CE00C43D881120").first.value)

    @Test
    fun fiveLessThanFifteen() = assertEquals(1, toPacket("D8005AC2A8F0").first.value)

    @Test
    fun fiveNotGreaterThanFifteen() = assertEquals(0, toPacket("F600BC2D8F").first.value)

    @Test
    fun fiveNotEqualFifteen() = assertEquals(0, toPacket("9C005AC2F8F0").first.value)

    @Test
    fun arithmetic() = assertEquals(1, toPacket("9C0141080250320F1802104A08").first.value)

    @Test
    fun partTwo() {
        val inputPacket = toPacket(input.first()).first
        assertEquals(186189840660, inputPacket.value)
    }
}

data class Packet(val version: Int, val type: Int, val sub: List<Packet> = listOf(), val value: Long)

private fun Packet.sumVersion(): Int = this.version + sub.sumOf { it.sumVersion() }

val mask = listOf(
    0b1000,
    0b0100,
    0b0010,
    0b0001,
)

private const val VER_END = 3
private const val TYPE_END = 6
private const val LEN_TYPE = 6
private const val LEN_START = 7
private const val LEN_END_0 = 7 + 15
private const val LEN_END_1 = 7 + 11

private fun toPacket(input: String): Pair<Packet, List<Int>> {
    val bits = input.flatMap { hex ->
        hex.digitToInt(16).let { number ->
            mask.map { m -> number.and(m).coerceAtMost(1) }
        }
    }
    return toPacket(bits)
}

private fun toPacket(bits: List<Int>): Pair<Packet, List<Int>> {
    val ver = bits.numberAt(0, VER_END)

    return when (val type = bits.numberAt(VER_END, TYPE_END)) {
        4 -> { // literal value
            val chunks = bits.drop(TYPE_END).chunked(5)
            val numChunks = chunks.indexOfFirst { it[0] == 0 } + 1
            val value = chunks.take(numChunks).flatMap { it.drop(1) }.joinToString("").toLong(2)
            Packet(ver, type, value = value) to bits.drop(TYPE_END + numChunks * 5)
        }
        else -> {
            val (subs, rest) = when (bits[LEN_TYPE]) {
                0 -> {
                    val len = bits.numberAt(LEN_START, LEN_END_0)
                    var subPacketBits = bits.subList(LEN_END_0, LEN_END_0 + len)
                    val packets = mutableListOf<Packet>()
                    while (subPacketBits.isNotEmpty()) {
                        with(toPacket(subPacketBits)) {
                            packets.add(this.first)
                            subPacketBits = this.second
                        }
                    }
                    packets to bits.drop(LEN_END_0 + len)
                }
                1 -> {
                    var packetData = bits.drop(LEN_END_1)
                    (1..bits.numberAt(LEN_START, LEN_END_1)).map {
                        toPacket(packetData).also { (_, rest) ->
                            packetData = rest
                        }.first
                    } to packetData
                }
                else -> error("bad len type")
            }
            val value: Long = when (type) {
                0 -> subs.sumOf { it.value } // sum
                1 -> subs.map { it.value }.reduce { a, b -> a * b } // product
                2 -> subs.minOf { it.value } // min
                3 -> subs.maxOf { it.value } // max
                5 -> (if (subs[0].value > subs[1].value) 1 else 0) // greater than
                6 -> (if (subs[0].value < subs[1].value) 1 else 0) // less than
                7 -> (if (subs[0].value == subs[1].value) 1 else 0) // equal
                else -> error("bad type")
            }

            Packet(ver, type, subs, value) to rest
        }
    }
}

private fun <E> List<E>.numberAt(start: Int, end: Int): Int = this.subList(start, end).joinToString("").toInt(2)

