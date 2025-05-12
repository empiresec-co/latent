package co.empiresec.possibilities.nip01Core.signers

import co.empiresec.possibilities.EventFactory
import co.empiresec.possibilities.nip01Core.Event
import co.empiresec.possibilities.nip01Core.HexKey
import co.empiresec.possibilities.nip01Core.crypto.EventHasher
import co.empiresec.possibilities.nip04Dm.crypto.EncryptedInfo
import co.empiresec.possibilities.nip57Zaps.LnZapPrivateEvent
import co.empiresec.possibilities.nip57Zaps.LnZapRequestEvent

abstract class NostrSigner(
    val pubKey: HexKey,
) {
    fun <T : Event> sign(
        ev: EventTemplate<T>,
        onReady: (T) -> Unit,
    ) = sign(ev.createdAt, ev.kind, ev.tags, ev.content, onReady)

    abstract fun <T : Event> sign(
        createdAt: Long,
        kind: Int,
        tags: Array<Array<String>>,
        content: String,
        onReady: (T) -> Unit,
    )

    abstract fun nip04Encrypt(
        decryptedContent: String,
        toPublicKey: HexKey,
        onReady: (String) -> Unit,
    )

    abstract fun nip04Decrypt(
        encryptedContent: String,
        fromPublicKey: HexKey,
        onReady: (String) -> Unit,
    )

    abstract fun nip44Encrypt(
        decryptedContent: String,
        toPublicKey: HexKey,
        onReady: (String) -> Unit,
    )

    abstract fun nip44Decrypt(
        encryptedContent: String,
        fromPublicKey: HexKey,
        onReady: (String) -> Unit,
    )

    abstract fun decryptZapEvent(
        event: LnZapRequestEvent,
        onReady: (LnZapPrivateEvent) -> Unit,
    )

    abstract fun deriveKey(
        nonce: HexKey,
        onReady: (HexKey) -> Unit,
    )

    fun decrypt(
        encryptedContent: String,
        fromPublicKey: HexKey,
        onReady: (String) -> Unit,
    ) {
        if (EncryptedInfo.isNIP04(encryptedContent)) {
            nip04Decrypt(encryptedContent, fromPublicKey, onReady)
        } else {
            nip44Decrypt(encryptedContent, fromPublicKey, onReady)
        }
    }

    fun <T : Event> assembleRumor(
        ev: EventTemplate<T>,
        onReady: (T) -> Unit,
    ) = assembleRumor(ev.createdAt, ev.kind, ev.tags, ev.content, onReady)

    fun <T : Event> assembleRumor(
        createdAt: Long,
        kind: Int,
        tags: Array<Array<String>>,
        content: String,
        onReady: (T) -> Unit,
    ) {
        onReady(
            EventFactory.create(
                id = EventHasher.hashId(pubKey, createdAt, kind, tags, content),
                pubKey = pubKey,
                createdAt = createdAt,
                kind = kind,
                tags = tags,
                content = content,
                sig = "",
            ) as T,
        )
    }

    fun <T : Event> assembleRumor(ev: EventTemplate<T>) = assembleRumor<T>(ev.createdAt, ev.kind, ev.tags, ev.content)

    fun <T : Event> assembleRumor(
        createdAt: Long,
        kind: Int,
        tags: Array<Array<String>>,
        content: String,
    ) = EventFactory.create(
        id = EventHasher.hashId(pubKey, createdAt, kind, tags, content),
        pubKey = pubKey,
        createdAt = createdAt,
        kind = kind,
        tags = tags,
        content = content,
        sig = "",
    ) as T
}
