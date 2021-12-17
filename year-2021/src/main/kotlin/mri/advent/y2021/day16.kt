package mri.advent.y2021

import java.math.BigInteger

/**
 * --- Day 16: Packet Decoder ---
 */
class Day16(val data: String = "/day16.in") {

    data class Packet(val version: Long, val type: Long, var length: Int, val value: Long? = null, val subGroups: MutableList<Packet> = mutableListOf()) {
        fun addGroup(group: Packet) {
            subGroups.add(group)
            length += group.length
        }

        // calculate packet version sum (part 1)
        fun versionSum(): Long = this.version + subGroups.sumOf { it.versionSum() }

        // calculate packet value (part 2)
        fun value(): Long {
            val groupsValues = this.subGroups.map { it.value() }
            return when (type.toInt()) {
                0 -> groupsValues.sum();
                1 -> groupsValues.reduce { a, b -> a * b }
                2 -> groupsValues.minOf { it }
                3 -> groupsValues.maxOf { it }
                4 -> value!!
                5 -> if (groupsValues[0] > groupsValues[1]) 1L else 0L
                6 -> if (groupsValues[0] < groupsValues[1]) 1L else 0L
                7 -> if (groupsValues[0] == groupsValues[1]) 1L else 0L;
                else -> throw  IllegalStateException("unsupported group id! $type");
            };
        }
    }

    fun hexaToBinary(hex: String)= hex
        .map { BigInteger("$it", 16).toString(2).padStart(4, '0') }
        .joinToString("")

    fun binaryToDecimal(binary: String) =  BigInteger(binary, 2).toString(10).toLong()

    // packet format:  VVV TTT
    fun recurseParsePacket(str: String, padding: Int = 0): Packet {
        val version = binaryToDecimal(str.substring(0, 3))
        val typeId = binaryToDecimal(str.substring(3, 6))

        if (typeId == 4L) { // literal value packet
            var position = 6
            val parts = mutableListOf<String>()
            while (position < str.length) {
                parts.add(str.substring(position, position + 5))
                position += 5
                if (parts.last()[0] == '0') break
            }
            val packet = Packet(version, 4, 6 + 5 * parts.size,
                binaryToDecimal(parts.map { it.substring(1) }.joinToString("")))
            debug("packet [version=$version,type=$typeId] ->> value=${packet.value} , parts=${parts.joinToString(" ") { it }}", padding)
            return packet

        } else { // operator packet
            val operatorPacket = Packet(version, typeId, 7)
            var subPacketsLength: Long? = null
            var subPacketsCount: Long? = null
            var subPacketsStart = 7
            if ('0' == str[6]) { // we know subgroups max length
                subPacketsLength = binaryToDecimal(str.substring(subPacketsStart, subPacketsStart + 15))
                subPacketsStart += 15
                operatorPacket.length += 15
            } else { // we know subgroups count
                subPacketsCount = binaryToDecimal(str.substring(subPacketsStart, subPacketsStart + 11))
                subPacketsStart += 11
                operatorPacket.length += 11
            }
            debug("packet [version=$version,type=$typeId]  ->>  subPacketsLength=$subPacketsLength, subPacketsCount=$subPacketsCount", padding)
            while (subPacketsLength != null && operatorPacket.subGroups.sumOf { it.length } < subPacketsLength
                || subPacketsCount != null && operatorPacket.subGroups.count() < subPacketsCount) {
                operatorPacket.addGroup(recurseParsePacket(str.substring(subPacketsStart), padding + 4))
                subPacketsStart += operatorPacket.subGroups.last().length
            }
            return operatorPacket
        }
    }

    /**
     * PART 1 : Decode the structure of your hexadecimal-encoded BITS transmission;
     * what do you get if you add up the version numbers in all packets?
     */
    fun part1(): Any {
        val binaryMessage = hexaToBinary(resourceAsStrings(data)[0])
        val packet = recurseParsePacket(binaryMessage)
        return packet.versionSum()
    }
    /**
     * PART 2 : What do you get if you evaluate the expression represented by your hexadecimal-encoded BITS transmission?
     */
    fun part2(): Any {
        val binaryMessage = hexaToBinary(resourceAsStrings(data)[0])
        val packet = recurseParsePacket(binaryMessage)
        return packet.value()
    }
}

fun main() {
    println(" part1:" + Day16().part1())
    println(" part2:" + Day16().part2())
}

