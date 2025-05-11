package co.empiresec.posssiblities.nip59Giftwrap

import android.util.Log
import androidx.compose.runtime.Immutable
import co.empiresec.posssiblities.nip01Core.Event
import co.empiresec.posssiblities.nip01Core.HexKey
import co.empiresec.posssiblities.nip01Core.signers.NostrSigner
import co.empiresec.posssiblities.nip59Giftwrap.rumors.Rumor
import co.empiresec.latent.utils.TimeUtils
import co.empiresec.latent.utils.bytesUsedInMemory
import co.empiresec.latent.utils.pointerSizeInBytes

@Immutable
class SealedRumorEvent(
    id: HexKey,
    pubKey: HexKey,
    createdAt: Long,
    tags: Array<Array<String>>,
    content: String,
    sig: HexKey,
) : WrappedEvent(id, pubKey, createdAt, KIND, tags, content, sig) {
    @Transient var innerEventId: HexKey? = null

    override fun countMemory(): Long =
        super.countMemory() +
            pointerSizeInBytes + (innerEventId?.bytesUsedInMemory() ?: 0)

    fun copyNoContent(): SealedRumorEvent {
        val copy =
            SealedRumorEvent(
                id,
                pubKey,
                createdAt,
                tags,
                "",
                sig,
            )

        copy.host = host
        copy.innerEventId = innerEventId

        return copy
    }

    override fun isContentEncoded() = true

    @Deprecated(
        message = "Heavy caching was removed from this class due to high memory use. Cache it separatedly",
        replaceWith = ReplaceWith("unseal"),
    )
    fun cachedRumor(
        signer: NostrSigner,
        onReady: (Event) -> Unit,
    ) = unseal(signer, onReady)

    fun unseal(
        signer: NostrSigner,
        onReady: (Event) -> Unit,
    ) {
        try {
            plainContent(signer) {
                try {
                    val rumor = Rumor.fromJson(it)
                    val event = rumor.mergeWith(this)
                    if (event is WrappedEvent) {
                        event.host = host ?: HostStub(this.id, this.pubKey, this.kind)
                    }
                    innerEventId = event.id

                    onReady(event)
                } catch (e: Exception) {
                    Log.w("RumorEvent", "Fail to decrypt or parse Rumor", e)
                }
            }
        } catch (e: Exception) {
            Log.w("RumorEvent", "Fail to decrypt or parse Rumor", e)
        }
    }

    private fun plainContent(
        signer: NostrSigner,
        onReady: (String) -> Unit,
    ) {
        if (content.isEmpty()) return

        signer.nip44Decrypt(content, pubKey, onReady)
    }

    companion object {
        const val KIND = 13

        fun create(
            event: Event,
            encryptTo: HexKey,
            signer: NostrSigner,
            createdAt: Long = TimeUtils.now(),
            onReady: (SealedRumorEvent) -> Unit,
        ) {
            val rumor = Rumor.create(event)
            create(rumor, encryptTo, signer, createdAt, onReady)
        }

        fun create(
            rumor: Rumor,
            encryptTo: HexKey,
            signer: NostrSigner,
            createdAt: Long = TimeUtils.randomWithTwoDays(),
            onReady: (SealedRumorEvent) -> Unit,
        ) {
            val msg = Rumor.toJson(rumor)

            signer.nip44Encrypt(msg, encryptTo) { content ->
                signer.sign(createdAt, KIND, emptyArray(), content, onReady)
            }
        }
    }
}
