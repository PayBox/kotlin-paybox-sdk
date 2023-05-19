package money.paybox.payboxsdk.models

data class Payment(
    var status: String? = null,
    val paymentId: Int? = null,
    val redirectUrl: String? = null
)