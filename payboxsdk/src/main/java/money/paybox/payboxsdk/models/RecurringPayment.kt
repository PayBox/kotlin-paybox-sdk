package money.paybox.payboxsdk.models

data class RecurringPayment(
    val status: String?,
    val paymentId: Int?,
    val currency: String?,
    val amount: Float?,
    val recurringProfile: String?,
    val recurringExpireDate: String?
)