package co.empiresec.possibilities.nip47WalletConnect

import com.fasterxml.jackson.annotation.JsonProperty
import co.empiresec.latent.utils.bytesUsedInMemory
import co.empiresec.latent.utils.pointerSizeInBytes

// RESPONSE OBJECTS
abstract class Response(
    @JsonProperty("result_type") val resultType: String,
) {
    abstract fun countMemory(): Long
}

// PayInvoice Call

class PayInvoiceSuccessResponse(
    val result: PayInvoiceResultParams? = null,
) : Response("pay_invoice") {
    class PayInvoiceResultParams(
        val preimage: String,
    ) {
        fun countMemory(): Long = pointerSizeInBytes + preimage.bytesUsedInMemory()
    }

    override fun countMemory(): Long = pointerSizeInBytes + (result?.countMemory() ?: 0)
}

class PayInvoiceErrorResponse(
    val error: PayInvoiceErrorParams? = null,
) : Response("pay_invoice") {
    class PayInvoiceErrorParams(
        val code: ErrorType?,
        val message: String?,
    ) {
        fun countMemory(): Long = pointerSizeInBytes + pointerSizeInBytes + (message?.bytesUsedInMemory() ?: 0)
    }

    override fun countMemory(): Long = pointerSizeInBytes + (error?.countMemory() ?: 0)

    enum class ErrorType {
        @JsonProperty(value = "RATE_LIMITED")
        RATE_LIMITED,

        // The client is sending commands too fast. It should retry in a few seconds.
        @JsonProperty(value = "NOT_IMPLEMENTED")
        NOT_IMPLEMENTED,

        // The command is not known or is intentionally not implemented.
        @JsonProperty(value = "INSUFFICIENT_BALANCE")
        INSUFFICIENT_BALANCE,

        // The wallet does not have enough funds to cover a fee reserve or the payment amount.
        @JsonProperty(value = "QUOTA_EXCEEDED")
        QUOTA_EXCEEDED,

        // The wallet has exceeded its spending quota.
        @JsonProperty(value = "RESTRICTED")
        RESTRICTED,

        // This public key is not allowed to do this operation.
        @JsonProperty(value = "UNAUTHORIZED")
        UNAUTHORIZED,

        // This public key has no wallet connected.
        @JsonProperty(value = "INTERNAL")
        INTERNAL,

        // An internal error.
        @JsonProperty(value = "OTHER")
        OTHER, // Other error.
    }
}
