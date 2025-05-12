package co.empiresec.latent.utils

import java.security.SecureRandom

object RandomInstance {
    private val randomizer = SecureRandom()

    fun int(bound: Int = Int.MAX_VALUE) = randomizer.nextInt(bound)

    fun bytes(size: Int) = ByteArray(size).also { randomizer.nextBytes(it) }
}
