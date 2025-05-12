package co.empiresec.possibilities.nip17e2eeDm.files

import androidx.compose.runtime.Immutable
import co.empiresec.possibilities.nip01Core.HexKey
import co.empiresec.possibilities.nip01Core.TagArrayBuilder
import co.empiresec.possibilities.nip01Core.hints.EventHintBundle
import co.empiresec.possibilities.nip01Core.signers.eventTemplate
import co.empiresec.possibilities.nip01Core.tags.events.ETag
import co.empiresec.possibilities.nip01Core.tags.people.PTag
import co.empiresec.possibilities.nip17e2eeDm.base.BaseDMGroupEvent
import co.empiresec.possibilities.nip17e2eeDm.files.encryption.AESGCM
import co.empiresec.possibilities.nip17e2eeDm.files.tags.EncryptionAlgo
import co.empiresec.possibilities.nip17e2eeDm.files.tags.EncryptionKey
import co.empiresec.possibilities.nip17e2eeDm.files.tags.EncryptionNonce
import co.empiresec.possibilities.nip17e2eeDm.files.tags.FileTypeTag
import co.empiresec.possibilities.nip17e2eeDm.messages.ChatMessageEvent
import co.empiresec.possibilities.nip31Alts.alt
import co.empiresec.possibilities.nip94FileMetadata.tags.BlurhashTag
import co.empiresec.possibilities.nip94FileMetadata.tags.DimensionTag
import co.empiresec.possibilities.nip94FileMetadata.tags.HashSha256Tag
import co.empiresec.possibilities.nip94FileMetadata.tags.OriginalHashTag
import co.empiresec.possibilities.nip94FileMetadata.tags.SizeTag
import co.empiresec.latent.utils.TimeUtils

@Immutable
class ChatMessageEncryptedFileHeaderEvent(
    id: HexKey,
    pubKey: HexKey,
    createdAt: Long,
    tags: Array<Array<String>>,
    content: String,
    sig: HexKey,
) : BaseDMGroupEvent(id, pubKey, createdAt, KIND, tags, content, sig) {
    fun replyTo() = tags.mapNotNull(ETag::parseId)

    fun url() = content

    fun mimeType() = tags.firstNotNullOfOrNull(FileTypeTag::parse)

    fun hash() = tags.firstNotNullOfOrNull(HashSha256Tag::parse)

    fun size() = tags.firstNotNullOfOrNull(SizeTag::parse)

    fun dimensions() = tags.firstNotNullOfOrNull(DimensionTag::parse)

    fun blurhash() = tags.firstNotNullOfOrNull(BlurhashTag::parse)

    fun originalHash() = tags.firstNotNullOfOrNull(OriginalHashTag::parse)

    fun algo() = tags.firstNotNullOfOrNull(EncryptionAlgo::parse)

    fun key() = tags.firstNotNullOfOrNull(EncryptionKey::parse)

    fun nonce() = tags.firstNotNullOfOrNull(EncryptionNonce::parse)

    companion object {
        const val KIND = 15
        const val ALT_DESCRIPTION = "Encrypted file in chat"

        fun build(
            to: List<PTag>,
            url: String,
            cipher: AESGCM,
            replyTo: EventHintBundle<ChatMessageEvent>? = null,
            mimeType: String? = null,
            hash: String? = null,
            size: Int? = null,
            dimension: DimensionTag? = null,
            blurhash: String? = null,
            originalHash: String? = null,
            magnetUri: String? = null,
            torrentInfoHash: String? = null,
            createdAt: Long = TimeUtils.now(),
            initializer: TagArrayBuilder<ChatMessageEncryptedFileHeaderEvent>.() -> Unit = {},
        ) = eventTemplate(KIND, url, createdAt) {
            alt(ALT_DESCRIPTION)

            group(to)

            encryptionAlgo(cipher.name())
            encryptionKey(cipher.keyBytes)
            encryptionNonce(cipher.nonce)

            replyTo?.let { reply(replyTo) }

            hash?.let { hash(it) }
            size?.let { fileSize(it) }
            mimeType?.let { mimeType(it) }
            dimension?.let { dimension(it) }
            blurhash?.let { blurhash(it) }
            originalHash?.let { originalHash(it) }
            magnetUri?.let { magnet(it) }
            torrentInfoHash?.let { torrentInfohash(it) }

            initializer()
        }
    }
}
