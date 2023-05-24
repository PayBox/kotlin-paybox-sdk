package money.paybox.payboxsdk.models

data class Payment(
    var status: String? = null,
    val paymentId: Int? = null,
    val redirectUrl: String? = null,
    val merchantId: Int? = null,
    val orderId: Int? = null
)