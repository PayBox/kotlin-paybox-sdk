package money.paybox.payboxsdk.models

data class Card(
    val status: String?,
    val merchantId: String?,
    val cardId: String?,
    val recurringProfile: String?,
    val cardhash: String?,
    val date: String?
)