package co.empiresec.possibilities.nip01Core.relay.commands.toRelay

import com.fasterxml.jackson.databind.JsonNode
import co.empiresec.possibilities.latent.jackson.EventMapper
import co.empiresec.possibilities.nip42RelayAuth.RelayAuthEvent

class AuthCmd(
    val event: RelayAuthEvent,
) : Command {
    companion object {
        const val LABEL = "AUTH"

        @JvmStatic
        fun toJson(authEvent: RelayAuthEvent): String = """["AUTH",${authEvent.toJson()}]"""

        @JvmStatic
        fun parse(msgArray: JsonNode): AuthCmd =
            AuthCmd(
                EventMapper.fromJson(msgArray.get(1)) as RelayAuthEvent,
            )
    }
}
