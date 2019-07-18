package money.paybox.payboxsdk.models

data class Payment(
    open var status: String?,
    open val paymentId: Int?,
    val redirectUrl: String?
)