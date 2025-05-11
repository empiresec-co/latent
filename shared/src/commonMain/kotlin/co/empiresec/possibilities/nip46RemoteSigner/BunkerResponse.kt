package co.empiresec.possibilities.nip46RemoteSigner

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import co.empiresec.latent.utils.Hex
import co.empiresec.latent.utils.bytesUsedInMemory
import co.empiresec.latent.utils.pointerSizeInBytes

open class BunkerResponse(
    val id: String,
    val result: String?,
    val error: String?,
) : BunkerMessage() {
    override fun countMemory(): Long =
        3 * pointerSizeInBytes + // 3 fields, 4 bytes each reference (32bit)
            id.bytesUsedInMemory() +
            (result?.bytesUsedInMemory() ?: 0) +
            (error?.bytesUsedInMemory() ?: 0)

    class BunkerResponseDeserializer : StdDeserializer<BunkerResponse>(BunkerResponse::class.java) {
        override fun deserialize(
            jp: JsonParser,
            ctxt: DeserializationContext,
        ): BunkerResponse {
            val jsonObject: JsonNode = jp.codec.readTree(jp)

            val id = jsonObject.get("id").asText().intern()
            val result = jsonObject.get("result")?.asText()
            val error = jsonObject.get("error")?.asText()

            if (error != null) {
                return com.vitorpamplona.quartz.nip46RemoteSigner.BunkerResponseError
                    .parse(id, result, error)
            }

            if (result != null) {
                when (result) {
                    BunkerResponseAck.RESULT -> return BunkerResponseAck.parse(id, result, error)
                    BunkerResponsePong.RESULT -> return BunkerResponsePong.parse(id, result, error)
                    else -> {
                        if (result.length == 64 && Hex.isHex(result)) {
                            return BunkerResponsePublicKey.parse(id, result)
                        }

                        if (result.get(0) == '{') {
                            try {
                                return com.vitorpamplona.quartz.nip46RemoteSigner.BunkerResponseEvent
                                    .parse(id, result)
                            } catch (_: Exception) {
                            }

                            try {
                                return BunkerResponseGetRelays.parse(id, result)
                            } catch (_: Exception) {
                            }
                        }

                        return BunkerResponse(id, result, error)
                    }
                }
            }

            return BunkerResponse(id, result, error)
        }
    }

    class BunkerResponseSerializer : StdSerializer<BunkerResponse>(BunkerResponse::class.java) {
        override fun serialize(
            value: BunkerResponse,
            gen: JsonGenerator,
            provider: SerializerProvider,
        ) {
            gen.writeStartObject()
            gen.writeStringField("id", value.id)
            value.result?.let { gen.writeStringField("result", value.result) }
            value.error?.let { gen.writeStringField("error", it) }
            gen.writeEndObject()
        }
    }
}
