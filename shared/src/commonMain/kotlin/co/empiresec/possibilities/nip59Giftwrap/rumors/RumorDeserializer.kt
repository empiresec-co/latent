package co.empiresec.possibilities.nip59Giftwrap.rumors

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import co.empiresec.latent.jackson.toTypedArray

class RumorDeserializer : StdDeserializer<Rumor>(Rumor::class.java) {
    override fun deserialize(
        jp: JsonParser,
        ctxt: DeserializationContext,
    ): Rumor {
        val jsonObject: JsonNode = jp.codec.readTree(jp)
        return Rumor(
            id = jsonObject.get("id")?.asText()?.intern(),
            pubKey = jsonObject.get("pubkey")?.asText()?.intern(),
            createdAt = jsonObject.get("created_at")?.asLong(),
            kind = jsonObject.get("kind")?.asInt(),
            tags =
                jsonObject.get("tags").toTypedArray {
                    it.toTypedArray { s -> if (s.isNull) "" else s.asText().intern() }
                },
            content = jsonObject.get("content")?.asText(),
        )
    }
}
