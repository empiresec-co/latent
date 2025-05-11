package co.empirsec.possibilities.nip01Core

import co.empirsec.latent.utils.Hex

/** Makes the distinction between String and Hex * */
typealias HexKey = String

fun ByteArray.toHexKey(): HexKey = Hex.encode(this)

fun HexKey.hexToByteArray(): ByteArray = Hex.decode(this)

const val PUBKEY_LENGTH = 64

const val EVENT_ID_LENGTH = 64
