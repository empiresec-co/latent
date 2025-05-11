package co.empiresec.posssibilites.nip59Giftwrap.rumors

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class RumorSerializer : StdSerializer<Rumor>(Rumor::class.java) {
    override fun serialize(
        event: Rumor,
        gen: JsonGenerator,
        provider: SerializerProvider,
    ) {
        gen.writeStartObject()
        event.id?.let { gen.writeStringField("id", it) }
        event.pubKey?.let { gen.writeStringField("pubkey", it) }
        event.createdAt?.let { gen.writeNumberField("created_at", it) }
        event.kind?.let { gen.writeNumberField("kind", it) }
        event.tags?.let {
            gen.writeArrayFieldStart("tags")
            event.tags.forEach { tag -> gen.writeArray(tag, 0, tag.size) }
            gen.writeEndArray()
        }
        event.content?.let { gen.writeStringField("content", it) }
        gen.writeEndObject()
    }
}
