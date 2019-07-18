package money.paybox.payboxsdk.models

data class Status(
    val status: String?,
    val paymentId: Int?,
    val transactionStatus: String?,
    val canReject: String?,
    val isCaptured: String?,
    val cardPan: String?,
    val createDate: String?

)