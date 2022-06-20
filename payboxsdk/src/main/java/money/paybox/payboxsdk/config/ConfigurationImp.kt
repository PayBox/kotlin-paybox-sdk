package money.paybox.payboxsdk.config

import android.text.TextUtils
import money.paybox.payboxsdk.api.Params
import money.paybox.payboxsdk.api.Urls
import money.paybox.payboxsdk.interfaces.Configuration
import kotlin.collections.HashMap

class ConfigurationImp(val merchantId: Int): Configuration {

    private var userPhone: String? = null
    private var userEmail: String? = null
    private var testMode = true
    private var paymentSystem = PaymentSystem.NONE
    private var requestMethod = RequestMethod.POST
    private var language = Language.ru
    private var autoClearing = false
    private var encoding = "UTF-8"
    private var paymentLifetime = 300
    private var recurringLifetime = 36
    private var recurringMode = false
    private var checkUrl: String? = null
    private var resultUrl: String? = "https://paybox.kz"
    private var refundUrl: String? = null
    private var captureUrl: String? = null
    private var currencyCode: String = "KZT"
    private var isFrameRequired = false

    override fun setUserPhone(userPhone: String) {
        this.userPhone = userPhone
    }

    override fun setUserEmail(userEmail: String) {
        this.userEmail = userEmail
    }

    override fun testMode(enabled: Boolean) {
        this.testMode = enabled
    }

    override fun setPaymentSystem(paymentSystem: PaymentSystem) {
        this.paymentSystem = paymentSystem
    }

    override fun setRequestMethod(requestMethod: RequestMethod) {
        this.requestMethod = requestMethod
    }

    override fun setLanguage(language: Language) {
        this.language = language
    }

    override fun autoClearing(enabled: Boolean) {
        this.autoClearing = enabled
    }

    override fun setEncoding(encoding: String) {
        this.encoding = encoding
    }

    override fun setRecurringLifetime(lifetime: Int) {
        this.recurringLifetime = lifetime
    }

    override fun setPaymentLifetime(lifetime: Int) {
        this.paymentLifetime = lifetime
    }

    override fun recurringMode(enabled: Boolean) {
        this.recurringMode = enabled
    }

    override fun setCheckUrl(url: String) {
        this.checkUrl = url
    }

    override fun setResultUrl(url: String) {
        this.resultUrl = url
    }

    override fun setRefundUrl(url: String) {
        this.refundUrl = url
    }

    override fun setClearingUrl(url: String) {
        this.captureUrl = url
    }

    override fun setCurrencyCode(code: String) {
        this.currencyCode = code
    }

    override fun setFrameRequired(isRequired: Boolean) {
        this.isFrameRequired = isRequired
    }

    private fun Boolean.stringValue(): String {
        return if (this) {
            "1"
        } else {
            "0"
        }
    }


    fun defParams(): HashMap<String, String> {
        val params = HashMap<String, String>()
        params[Params.MERCHANT_ID] = this.merchantId.toString()
        params[Params.TEST_MODE] = this.testMode.stringValue()
        params[Params.PAYMENT_SYSTEM] = if(this.paymentSystem.name == "NONE") "" else this.paymentSystem.name
        return params
    }

    fun getParams(extraParams: HashMap<String, String>? = null): HashMap<String, String> {
        val params = HashMap<String, String>()
        extraParams?.let {
            if (extraParams.isNotEmpty()) {
                params.putAll(extraParams)
            }
        }
        params[Params.MERCHANT_ID] = this.merchantId.toString()
        params[Params.TEST_MODE] = this.testMode.stringValue()
        params[Params.RECURRING_START] = this.recurringMode.stringValue()
        params[Params.AUTOCLEARING] = this.autoClearing.stringValue()
        params[Params.REQUEST_METHOD] = this.requestMethod.name
        params[Params.CURRENCY] = this.currencyCode
        params[Params.LIFETIME] = this.paymentLifetime.toString()
        params[Params.ENCODING] = this.encoding
        params[Params.RECURRING_LIFETIME] = this.recurringLifetime.toString()
        params[Params.PAYMENT_SYSTEM] = if(this.paymentSystem.name == "NONE") "" else this.paymentSystem.name
        params[Params.PAYMENT_ROUTE] = if(this.isFrameRequired) "frame" else ""
        params[Params.TIMEOUT_AFTER_PAYMENT] = "0"
        params[Params.SUCCESS_METHOD] = "GET"
        params[Params.FAILURE_METHOD] = "GET"
        params[Params.SUCCESS_URL] = Urls.SUCCESS_URL
        params[Params.FAILURE_URL] = Urls.FAILURE_URL
        params[Params.BACK_LINK] = Urls.SUCCESS_URL
        params[Params.POST_LINK] = Urls.SUCCESS_URL
        params[Params.LANGUAGE] = this.language.name
        if (!TextUtils.isEmpty(this.userPhone)) {
            params[Params.USER_PHONE] = this.userPhone!!
        }
        if (!TextUtils.isEmpty(this.userEmail)) {
            params[Params.USER_CONTACT_EMAIL] = this.userEmail!!
            params[Params.USER_EMAIL] = this.userEmail!!
        }
        if (!TextUtils.isEmpty(this.captureUrl)) {
            params[Params.CAPTURE_URL] = this.captureUrl!!
        }
        if (!TextUtils.isEmpty(this.refundUrl)) {
            params[Params.REFUND_URL] = this.refundUrl!!
        }
        if (!TextUtils.isEmpty(this.resultUrl)) {
            params[Params.RESULT_URL] = this.resultUrl!!
        }
        if (!TextUtils.isEmpty(this.checkUrl)) {
            params[Params.CHECK_URL] = this.checkUrl!!
        }
        return params
    }

}