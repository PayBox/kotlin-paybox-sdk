package money.paybox.payboxsdk.models

data class ResponseData(
    val code: Int,
    val response: String,
    val url: String,
    val error: Boolean = code != 200
)