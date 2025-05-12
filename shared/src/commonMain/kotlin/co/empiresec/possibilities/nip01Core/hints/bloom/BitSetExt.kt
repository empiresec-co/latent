package co.empiresec.possibilities.nip01Core.hints.bloom

import java.util.BitSet

fun BitSet.printBits() =
    buildString {
        for (seed in 0 until size()) {
            append(if (this@printBits.get(seed)) "1" else "0")
        }
    }
