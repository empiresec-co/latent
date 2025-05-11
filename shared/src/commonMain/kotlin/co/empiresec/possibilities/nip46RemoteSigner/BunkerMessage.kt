package co.empiresec.possibilities.nip46RemoteSigner

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

abstract class BunkerMessage {
    abstract fun countMemory(): Long

    class BunkerMessageDeserializer : StdDeserializer<BunkerMessage>(BunkerMessage::class.java) {
        override fun deserialize(
            jp: JsonParser,
            ctxt: DeserializationContext,
        ): BunkerMessage {
            val jsonObject: JsonNode = jp.codec.readTree(jp)
            val isRequest = jsonObject.has("method")

            if (isRequest) {
                return jp.codec.treeToValue(jsonObject, BunkerRequest::class.java)
            } else {
                return jp.codec.treeToValue(jsonObject, BunkerResponse::class.java)
            }
        }
    }
}
