package money.paybox.payboxsdk.config

import money.paybox.payboxsdk.api.Params
import money.paybox.payboxsdk.api.Urls
import money.paybox.payboxsdk.config.Region.DEFAULT
import money.paybox.payboxsdk.interfaces.Configuration
import kotlin.collections.HashMap

class ConfigurationImp(val merchantId: Int) : Configuration {

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
    private var region: Region = DEFAULT

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

    override fun setFrameRequired(isFrameRequired: Boolean) {
        this.isFrameRequired = isFrameRequired
    }

    override fun setRegion(region: Region) {
        this.region = region
        Urls.region = region
    }

    private fun Boolean.stringValue(): String = if (this) "1" else "0"

    fun defParams(): HashMap<String, String> {
        val params = HashMap<String, String>()

        params.apply {
            put(Params.MERCHANT_ID, merchantId.toString())
            put(Params.TEST_MODE, testMode.stringValue())
            put(Params.PAYMENT_SYSTEM, if (paymentSystem.name == "NONE") "" else paymentSystem.name)
        }
        return params
    }

    fun getParams(extraParams: HashMap<String, String>? = null): HashMap<String, String> {
        val params = HashMap<String, String>()

        extraParams?.takeIf { it.isNotEmpty() }?.let { params.putAll(extraParams) }

        params.apply {
            put(Params.MERCHANT_ID, merchantId.toString())
            put(Params.TEST_MODE, testMode.stringValue())
            put(Params.AUTOCLEARING, autoClearing.stringValue())
            put(Params.REQUEST_METHOD, requestMethod.name)
            put(Params.CURRENCY, currencyCode)
            put(Params.LIFETIME, paymentLifetime.toString())
            put(Params.ENCODING, encoding)
            if(recurringMode) {
                put(Params.RECURRING_START, recurringMode.stringValue())
                put(Params.RECURRING_LIFETIME, recurringLifetime.toString())
            }
            put(
                Params.PAYMENT_SYSTEM,
                if (paymentSystem.name == Params.NONE) "" else paymentSystem.name
            )
            put(Params.PAYMENT_ROUTE, if (isFrameRequired) Params.FRAME else "")
            put(Params.TIMEOUT_AFTER_PAYMENT, "0")
            put(Params.SUCCESS_METHOD, Params.GET)
            put(Params.FAILURE_METHOD, Params.GET)
            put(Params.SUCCESS_URL, Urls.successUrl())
            put(Params.FAILURE_URL, Urls.failureUrl())
            put(Params.BACK_LINK, Urls.successUrl())
            put(Params.POST_LINK, Urls.successUrl())
            put(Params.LANGUAGE, language.name)

            userPhone?.takeIf { it.isNotEmpty() }?.let { put(Params.USER_PHONE, it) }
            userEmail?.takeIf { it.isNotEmpty() }?.let {
                put(Params.USER_CONTACT_EMAIL, it)
                put(Params.USER_EMAIL, it)
            }

            captureUrl?.takeIf { it.isNotEmpty() }?.let { put(Params.CAPTURE_URL, it) }
            refundUrl?.takeIf { it.isNotEmpty() }?.let { put(Params.REFUND_URL, it) }
            resultUrl?.takeIf { it.isNotEmpty() }?.let { put(Params.RESULT_URL, it) }
            checkUrl?.takeIf { it.isNotEmpty() }?.let { put(Params.CHECK_URL, it) }
        }

        return params
    }
}