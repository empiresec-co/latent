package co.empiresec.possibilities.nip01Core.jackson

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import co.empiresec.possibilities.nip01Core.Event
import co.empiresec.possibilities.nip46RemoteSigner.BunkerMessage
import co.empiresec.possibilities.nip46RemoteSigner.BunkerRequest
import co.empiresec.possibilities.nip46RemoteSigner.BunkerResponse
import co.empiresec.possibilities.nip47WalletConnect.Request
import co.empiresec.possibilities.nip47WalletConnect.RequestDeserializer
import co.empiresec.possibilities.nip47WalletConnect.Response
import co.empiresec.possibilities.nip47WalletConnect.ResponseDeserializer
import co.empiresec.possibilities.nip59Giftwrap.rumors.Rumor
import co.empiresec.possibilities.nip59Giftwrap.rumors.RumorDeserializer
import co.empiresec.possibilities.nip59Giftwrap.rumors.RumorSerializer

class EventMapper {
    companion object {
        val defaultPrettyPrinter = InliningTagArrayPrettyPrinter()

        val mapper =
            jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())
                .setDefaultPrettyPrinter(defaultPrettyPrinter)
                .registerModule(
                    SimpleModule()
                        .addSerializer(Event::class.java, EventSerializer())
                        .addDeserializer(Event::class.java, EventDeserializer())
                        .addSerializer(Rumor::class.java, RumorSerializer())
                        .addDeserializer(Rumor::class.java, RumorDeserializer())
                        .addDeserializer(Response::class.java, ResponseDeserializer())
                        .addDeserializer(Request::class.java, RequestDeserializer())
                        .addDeserializer(BunkerMessage::class.java, BunkerMessage.BunkerMessageDeserializer())
                        .addSerializer(BunkerRequest::class.java, BunkerRequest.BunkerRequestSerializer())
                        .addDeserializer(BunkerRequest::class.java, BunkerRequest.BunkerRequestDeserializer())
                        .addSerializer(BunkerResponse::class.java, BunkerResponse.BunkerResponseSerializer())
                        .addDeserializer(BunkerResponse::class.java, BunkerResponse.BunkerResponseDeserializer()),
                )

        fun fromJson(json: String): Event = mapper.readValue(json, Event::class.java)

        fun fromJson(json: JsonNode): Event = EventManualDeserializer.fromJson(json)

        fun toJson(event: Event): String = mapper.writeValueAsString(event)

        fun toJson(event: ArrayNode?): String = mapper.writeValueAsString(event)

        fun toJson(event: ObjectNode?): String = mapper.writeValueAsString(event)
    }
}
