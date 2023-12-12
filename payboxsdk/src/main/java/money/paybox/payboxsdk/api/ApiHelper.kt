package money.paybox.payboxsdk.api

import money.paybox.payboxsdk.config.RequestMethod
import money.paybox.payboxsdk.interfaces.ApiListener
import money.paybox.payboxsdk.models.RequestData
import kotlin.collections.HashMap

class ApiHelper(override val listener: ApiListener, override var secretKey: String) : BaseApi() {

    fun initConnection(url: String, params: HashMap<String, String>, paymentType: String? = null) {
        request {
            RequestData(params.signedParams(url), RequestMethod.POST, url, paymentType)
        }
    }
}