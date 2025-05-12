package co.empiresec.possibilities.nip01Core.relay.commands.toClient

import com.fasterxml.jackson.databind.JsonNode

class AuthMessage(
    val challenge: String,
) : Message {
    companion object {
        const val LABEL = "AUTH"

        @JvmStatic
        fun parse(msgArray: JsonNode): AuthMessage =
            AuthMessage(
                msgArray.get(1).asText(),
            )
    }
}
