package co.empiresec.possibilities.nip47WalletConnect

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class RequestDeserializer : StdDeserializer<Request>(Request::class.java) {
    override fun deserialize(
        jp: JsonParser,
        ctxt: DeserializationContext,
    ): Request? {
        val jsonObject: JsonNode = jp.codec.readTree(jp)
        val method = jsonObject.get("method")?.asText()

        if (method == "pay_invoice") {
            return jp.codec.treeToValue(jsonObject, PayInvoiceMethod::class.java)
        }
        return null
    }
}
