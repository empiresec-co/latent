package co.empiresec.latent.utils

object Hex {
    private const val LOWER_CASE_HEX = "0123456789abcdef"
    private const val UPPER_CASE_HEX = "0123456789ABCDEF"

    private val hexToByte: IntArray =
        IntArray(256) { -1 }.apply {
            LOWER_CASE_HEX.forEachIndexed { index, char -> this[char.code] = index }
            UPPER_CASE_HEX.forEachIndexed { index, char -> this[char.code] = index }
        }

    // Encodes both chars in a single Int variable
    private val byteToHex =
        IntArray(256) {
            (LOWER_CASE_HEX[(it shr 4)].code shl 8) or LOWER_CASE_HEX[(it and 0xF)].code
        }

    @JvmStatic
    fun isHex(hex: String?): Boolean {
        if (hex.isNullOrEmpty()) return false
        if (hex.length and 1 != 0) return false

        try {
            for (c in hex.indices) {
                if (hexToByte[hex[c].code] < 0) return false
            }
        } catch (e: IllegalArgumentException) {
            // there are p tags with emoji's which makes the hex[c].code > 256
            return false
        }

        return true
    }

    @JvmStatic
    fun decode(hex: String): ByteArray {
        // faster version of hex decoder
        require(hex.length and 1 == 0)
        return ByteArray(hex.length / 2) {
            (hexToByte[hex[2 * it].code] shl 4 or hexToByte[hex[2 * it + 1].code]).toByte()
        }
    }

    @JvmStatic
    fun encode(input: ByteArray): String {
        val out = CharArray(input.size * 2)
        var outIdx = 0
        for (i in 0 until input.size) {
            val chars = byteToHex[input[i].toInt() and 0xFF]
            out[outIdx++] = (chars shr 8).toChar()
            out[outIdx++] = (chars and 0xFF).toChar()
        }
        return String(out)
    }
}
