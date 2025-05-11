package co.empiresec.possibilities.nip47WalletConnect

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class ResponseDeserializer : StdDeserializer<Response>(Response::class.java) {
    override fun deserialize(
        jp: JsonParser,
        ctxt: DeserializationContext,
    ): Response? {
        val jsonObject: JsonNode = jp.codec.readTree(jp)
        val resultType = jsonObject.get("result_type")?.asText()

        if (resultType == "pay_invoice") {
            val result = jsonObject.get("result")
            val error = jsonObject.get("error")
            if (result != null) {
                return jp.codec.treeToValue(jsonObject, PayInvoiceSuccessResponse::class.java)
            }
            if (error != null) {
                return jp.codec.treeToValue(jsonObject, PayInvoiceErrorResponse::class.java)
            }
        } else {
            // tries to guess
            if (jsonObject.get("result")?.get("preimage") != null) {
                return jp.codec.treeToValue(jsonObject, PayInvoiceSuccessResponse::class.java)
            }
            if (jsonObject.get("error")?.get("code") != null) {
                return jp.codec.treeToValue(jsonObject, PayInvoiceErrorResponse::class.java)
            }
        }
        return null
    }
}
