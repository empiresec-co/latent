package co.empiresec.possibilities.nip46RemoteSigner

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import co.empiresec.latent.jackson.toTypedArray
import co.empiresec.latent.utils.bytesUsedInMemory
import co.empirsec.latent.utils.pointerSizeInBytes

open class BunkerRequest(
    val id: String,
    val method: String,
    val params: Array<String> = emptyArray(),
) : BunkerMessage() {
    override fun countMemory(): Long =
        3 * pointerSizeInBytes + // 3 fields, 4 bytes each reference (32bit)
            id.bytesUsedInMemory() +
            method.bytesUsedInMemory() +
            params.sumOf { pointerSizeInBytes + it.bytesUsedInMemory() }

    class BunkerRequestDeserializer : StdDeserializer<BunkerRequest>(BunkerRequest::class.java) {
        override fun deserialize(
            jp: JsonParser,
            ctxt: DeserializationContext,
        ): BunkerRequest {
            val jsonObject: JsonNode = jp.codec.readTree(jp)
            val id = jsonObject.get("id").asText().intern()
            val method = jsonObject.get("method").asText().intern()
            val params = jsonObject.get("params")?.toTypedArray { it.asText().intern() } ?: emptyArray()

            return when (method) {
                BunkerRequestConnect.METHOD_NAME -> BunkerRequestConnect.parse(id, params)
                BunkerRequestGetPublicKey.METHOD_NAME -> BunkerRequestGetPublicKey.parse(id, params)
                BunkerRequestGetRelays.METHOD_NAME -> BunkerRequestGetRelays.parse(id, params)

                BunkerRequestNip04Decrypt.METHOD_NAME -> BunkerRequestNip04Decrypt.parse(id, params)
                BunkerRequestNip04Encrypt.METHOD_NAME -> BunkerRequestNip04Encrypt.parse(id, params)
                BunkerRequestNip44Decrypt.METHOD_NAME -> BunkerRequestNip44Decrypt.parse(id, params)
                BunkerRequestNip44Encrypt.METHOD_NAME -> BunkerRequestNip44Encrypt.parse(id, params)

                BunkerRequestPing.METHOD_NAME -> BunkerRequestPing.parse(id, params)
                BunkerRequestSign.METHOD_NAME -> BunkerRequestSign.parse(id, params)
                else -> BunkerRequest(id, method, params)
            }
        }
    }

    class BunkerRequestSerializer : StdSerializer<BunkerRequest>(BunkerRequest::class.java) {
        override fun serialize(
            value: BunkerRequest,
            gen: JsonGenerator,
            provider: SerializerProvider,
        ) {
            gen.writeStartObject()
            gen.writeStringField("id", value.id)
            gen.writeStringField("method", value.method)
            gen.writeArrayFieldStart("params")
            value.params.forEach {
                gen.writeString(it)
            }
            gen.writeEndArray()
            gen.writeEndObject()
        }
    }
}
