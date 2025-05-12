package co.empiresec.possibilities.nip01Core.hints.bloom

import co.empiresec.latent.utils.RandomInstance
import java.util.Base64
import java.util.BitSet
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class BloomFilterMurMur3(
    private val size: Int,
    private val rounds: Int,
    private val bits: BitSet = BitSet(size),
    private val commonSalt: Int = RandomInstance.int(),
) {
    private val hasher = MurmurHash3()
    private val lock = ReentrantReadWriteLock()

    fun add(
        value: ByteArray,
        salt: Int = commonSalt,
    ) {
        lock.write {
            repeat(rounds) {
                bits.set(hash(value, salt + it))
            }
        }
    }

    fun mightContain(
        value: ByteArray,
        salt: Int = commonSalt,
    ): Boolean {
        lock.read {
            repeat(rounds) {
                if (!bits.get(hash(value, salt + it))) return false
            }
            return true
        }
    }

    private fun hash(
        value: ByteArray,
        seed: Int,
    ) = hasher.hash(value, seed).mod(size)

    fun encode() = encode(this)

    fun printBits() = bits.printBits()

    companion object {
        fun encode(f: BloomFilterMurMur3): String {
            val bitSetB64 = Base64.getEncoder().encodeToString(f.bits.toByteArray())
            return "${f.size}:${f.rounds}:$bitSetB64:${f.commonSalt}"
        }

        fun decode(encodedStr: String): BloomFilterMurMur3 {
            val (sizeStr, roundsStr, filterB64, salt) = encodedStr.split(":")
            val bitSet = BitSet.valueOf(Base64.getDecoder().decode(filterB64))
            return BloomFilterMurMur3(sizeStr.toInt(), roundsStr.toInt(), bitSet, salt.toInt())
        }
    }
}
