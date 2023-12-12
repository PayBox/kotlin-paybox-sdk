package money.paybox.payboxsdk.models

import money.paybox.payboxsdk.config.RequestMethod
import java.util.*

data class RequestData(
    val params: SortedMap<String, String>,
    val method: RequestMethod,
    val url: String,
    val paymentType: String? = null
)