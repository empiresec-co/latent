package co.empiresec.possibilities.nip47WalletConnect

// REQUEST OBJECTS
abstract class Request(
    var method: String? = null,
)

// PayInvoice Call
class PayInvoiceParams(
    var invoice: String? = null,
)

class PayInvoiceMethod(
    var params: PayInvoiceParams? = null,
) : Request("pay_invoice") {
    companion object {
        fun create(bolt11: String): PayInvoiceMethod = PayInvoiceMethod(PayInvoiceParams(bolt11))
    }
}
