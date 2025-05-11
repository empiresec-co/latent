package co.empiresec.possibilities.nip59Giftwrap

import android.util.Log
import androidx.compose.runtime.Immutable
import co.empiresec.possibilities.nip01Core.Event
import co.empiresec.possibilities.nip01Core.HexKey
import co.empiresec.possibilities.nip01Core.firstTagValue
import co.empiresec.possibilities.nip01Core.crypto.KeyPair
import cco.empiresec.possibilities.nip01Core.signers.NostrSigner
import co.empiresec.possibilities.signers.NostrSignerInternal
import co.empiresec.possibilities.nip21UriScheme.toNostrUri
import co.empiresec.latent.utis.TimeUtils
import co.empiresec.latent.utils.bytesUsedInMemory
import co.empiresec.latent.utils.pointerSizeInBytes

@Immutable
class GiftWrapEvent(
    id: HexKey,
    pubKey: HexKey,
    createdAt: Long,
    tags: Array<Array<String>>,
    content: String,
    sig: HexKey,
) : Event(id, pubKey, createdAt, KIND, tags, content, sig) {
    @Transient var innerEventId: HexKey? = null

    override fun countMemory(): Long =
        super.countMemory() +
            pointerSizeInBytes + (innerEventId?.bytesUsedInMemory() ?: 0)

    fun copyNoContent(): GiftWrapEvent {
        val copy =
            GiftWrapEvent(
                id,
                pubKey,
                createdAt,
                tags,
                "",
                sig,
            )

        copy.innerEventId = innerEventId

        return copy
    }

    override fun isContentEncoded() = true

    @Deprecated(
        message = "Heavy caching was removed from this class due to high memory use. Cache it separatedly",
        replaceWith = ReplaceWith("unwrap"),
    )
    fun cachedGift(
        signer: NostrSigner,
        onReady: (Event) -> Unit,
    ) = unwrap(signer, onReady)

    fun unwrapThrowing(
        signer: NostrSigner,
        onReady: (Event) -> Unit,
    ) {
        plainContent(signer) { giftStr ->
            val gift = fromJson(giftStr)

            if (gift is WrappedEvent) {
                gift.host = HostStub(this.id, this.pubKey, this.kind)
            }
            innerEventId = gift.id

            onReady(gift)
        }
    }

    fun unwrap(
        signer: NostrSigner,
        onReady: (Event) -> Unit,
    ) {
        try {
            unwrapThrowing(signer, onReady)
        } catch (e: Exception) {
            Log.w("GiftWrapEvent", "Couldn't Decrypt the content " + this.toNostrUri())
        }
    }

    private fun plainContent(
        signer: NostrSigner,
        onReady: (String) -> Unit,
    ) {
        if (content.isEmpty()) return

        signer.nip44Decrypt(content, pubKey, onReady)
    }

    fun recipientPubKey() = tags.firstTagValue("p")

    companion object {
        const val KIND = 1059
        const val ALT = "Encrypted event"

        fun create(
            event: Event,
            recipientPubKey: HexKey,
            createdAt: Long = TimeUtils.randomWithTwoDays(),
            onReady: (GiftWrapEvent) -> Unit,
        ) {
            val signer = NostrSignerInternal(KeyPair()) // GiftWrap is always a random key
            val serializedContent = event.toJson()
            val tags = arrayOf(arrayOf("p", recipientPubKey))

            signer.nip44Encrypt(serializedContent, recipientPubKey) { content ->
                signer.sign(createdAt, KIND, tags, content, onReady)
            }
        }
    }
}
