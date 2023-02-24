package money.paybox.payboxsdk.models

data class Payment(
    open var status: String? = null,
    open val paymentId: Int? = null,
    val redirectUrl: String? = null,
    val merchantId: Int? = null,
    val orderId: Int? = null
)