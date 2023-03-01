package money.paybox.payboxsdk.models

data class Card(
    val status: String?,
    val merchantId: String?,
    val cardId: String? = null,
    val recurringProfile: String?,
    val cardhash: String?,
    val date: String?,
    val cardToken: String? = null
)