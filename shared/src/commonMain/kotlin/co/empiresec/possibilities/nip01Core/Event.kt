package co.empiresec.possibilities.nip01Core

import androidx.compose.runtime.Immutable
import com.fasterxml.jackson.annotation.JsonProperty
import co.empiresec.latent.jackson.EventManualSerializer
import co.empiresec.latent.jackson.EventMapper
import co.empiresec.possibilities.nip01Core.signers.eventTemplate
import co.empiresec.latent.utils.TimeUtils
import co.empiresec.latent.utils.bytesUsedInMemory
import co.empiresec.latent.utils.pointerSizeInBytes

@Immutable
open class Event(
    val id: HexKey,
    @JsonProperty("pubkey") val pubKey: HexKey,
    @JsonProperty("created_at") val createdAt: Long,
    val kind: Int,
    val tags: TagArray,
    val content: String,
    val sig: HexKey,
) : IEvent {
    open fun isContentEncoded() = false

    open fun countMemory(): Long =
        7 * pointerSizeInBytes + // 7 fields, 4 bytes each reference (32bit)
            12L + // createdAt + kind
            id.bytesUsedInMemory() +
            pubKey.bytesUsedInMemory() +
            tags.sumOf { pointerSizeInBytes + it.sumOf { pointerSizeInBytes + it.bytesUsedInMemory() } } +
            content.bytesUsedInMemory() +
            sig.bytesUsedInMemory()

    fun toJson(): String = EventManualSerializer.toJson(id, pubKey, createdAt, kind, tags, content, sig)

    /**
     * For debug purposes only
     */
    fun toPrettyJson(): String = EventManualSerializer.toPrettyJson(id, pubKey, createdAt, kind, tags, content, sig)

    companion object {
        fun fromJson(json: String): Event = EventMapper.fromJson(json)

        fun build(
            kind: Int,
            content: String = "",
            createdAt: Long = TimeUtils.now(),
            initializer: TagArrayBuilder<Event>.() -> Unit = {},
        ) = eventTemplate(kind, content, createdAt, initializer)
    }
}
