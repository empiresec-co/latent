package co.empiresec.possibilities.nip01Core.crypto

import co.empiresec.possibilities.nip01Core.toHexKey

class KeyPair(
    privKey: ByteArray? = null,
    pubKey: ByteArray? = null,
    forceReplacePubkey: Boolean = true,
) {
    val privKey: ByteArray?
    val pubKey: ByteArray

    init {
        if (privKey == null) {
            if (pubKey == null) {
                // create new, random keys
                this.privKey = Nip01.privKeyCreate()
                this.pubKey = Nip01.pubKeyCreate(this.privKey)
            } else {
                // this is a read-only account
                check(pubKey.size == 32)
                this.privKey = null
                this.pubKey = pubKey
            }
        } else {
            // as private key is provided, ignore the public key and set keys according to private key
            this.privKey = privKey
            if (pubKey == null || forceReplacePubkey) {
                this.pubKey = Nip01.pubKeyCreate(privKey)
            } else {
                this.pubKey = pubKey
            }
        }
    }

    override fun toString(): String = "KeyPair(privateKey=${privKey?.toHexKey()}, publicKey=${pubKey.toHexKey()}"
}
