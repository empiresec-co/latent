package co.empiresec.possibilities.nip59Giftwrap.rumors

import com.fasterxml.jackson.annotation.JsonProperty
import co.empiresec.possibilities.EventFactory
import co.empiresec.possibilities.nip01Core.Event
import co.empiresec.possibilities.nip01Core.HexKey
import co.empiresec.possibilities.nip01Core.crypto.EventHasher
import co.empiresec.latent.jackson.EventMapper
import co.empiresec.possibilities.nip59Giftwrap.SealedRumorEvent

class Rumor(
    val id: HexKey?,
    @JsonProperty("pubkey") val pubKey: HexKey?,
    @JsonProperty("created_at") val createdAt: Long?,
    val kind: Int?,
    val tags: Array<Array<String>>?,
    val content: String?,
) {
    fun mergeWith(event: SealedRumorEvent): Event {
        val newPubKey = event.pubKey // forces to be the pubkey of the seal to make sure impersonators don't impersonate
        val newCreatedAt = if (createdAt != null && createdAt > 1000) createdAt else event.createdAt
        val newKind = kind ?: -1
        val newTags = (tags ?: emptyArray()).plus(event.tags)
        val newContent = content ?: ""
        val newID = id?.ifBlank { null } ?: EventHasher.hashId(newPubKey, newCreatedAt, newKind, newTags, newContent)
        val sig = ""

        return EventFactory.create(newID, newPubKey, newCreatedAt, newKind, newTags, newContent, sig)
    }

    companion object {
        fun fromJson(json: String): Rumor = EventMapper.mapper.readValue(json, Rumor::class.java)

        fun toJson(event: Rumor): String = EventMapper.mapper.writeValueAsString(event)

        fun create(event: Event): Rumor = Rumor(event.id, event.pubKey, event.createdAt, event.kind, event.tags, event.content)
    }
}
