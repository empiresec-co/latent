package co.empiresec.possibilities.nip01Core.signers

import co.empiresec.possibilities.nip01Core.Event
import co.empiresec.possibilities.nip01Core.HexKey
import co.empiresec.possibilities.nip01Core.toHexKey
import co.empiresec.possibilities.nip01Core.crypto.KeyPair
import co.empiresec.possibilities.nip57Zaps.LnZapPrivateEvent
import co.empiresec.possibilities.nip57Zaps.LnZapRequestEvent

class NostrSignerInternal(
    val keyPair: KeyPair,
) : NostrSigner(keyPair.pubKey.toHexKey()) {
    val signerSync = NostrSignerSync(keyPair)

    override fun <T : Event> sign(
        createdAt: Long,
        kind: Int,
        tags: Array<Array<String>>,
        content: String,
        onReady: (T) -> Unit,
    ) {
        signerSync.sign<T>(createdAt, kind, tags, content)?.let { onReady(it) }
    }

    override fun nip04Encrypt(
        decryptedContent: String,
        toPublicKey: HexKey,
        onReady: (String) -> Unit,
    ) {
        signerSync.nip04Encrypt(decryptedContent, toPublicKey)?.let { onReady(it) }
    }

    override fun nip04Decrypt(
        encryptedContent: String,
        fromPublicKey: HexKey,
        onReady: (String) -> Unit,
    ) {
        signerSync.nip04Decrypt(encryptedContent, fromPublicKey)?.let { onReady(it) }
    }

    override fun nip44Encrypt(
        decryptedContent: String,
        toPublicKey: HexKey,
        onReady: (String) -> Unit,
    ) {
        signerSync.nip44Encrypt(decryptedContent, toPublicKey)?.let { onReady(it) }
    }

    override fun nip44Decrypt(
        encryptedContent: String,
        fromPublicKey: HexKey,
        onReady: (String) -> Unit,
    ) {
        signerSync.nip44Decrypt(encryptedContent, fromPublicKey)?.let { onReady(it) }
    }

    override fun decryptZapEvent(
        event: LnZapRequestEvent,
        onReady: (LnZapPrivateEvent) -> Unit,
    ) {
        signerSync.decryptZapEvent(event)?.let { onReady(it) }
    }

    override fun deriveKey(
        nonce: HexKey,
        onReady: (HexKey) -> Unit,
    ) {
        signerSync.deriveKey(nonce)?.let { onReady(it) }
    }
}
