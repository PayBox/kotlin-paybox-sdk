package money.paybox.payboxsdk.models

data class Capture(
    val status: String?,
    val amount: Float?,
    val clearingAmount: Float?
)