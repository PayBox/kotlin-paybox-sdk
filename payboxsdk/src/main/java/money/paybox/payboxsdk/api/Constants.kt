package money.paybox.payboxsdk.api

import money.paybox.payboxsdk.config.Region
import money.paybox.payboxsdk.config.Region.DEFAULT
import money.paybox.payboxsdk.config.Region.KG
import money.paybox.payboxsdk.config.Region.RU
import money.paybox.payboxsdk.config.Region.UZ

object Urls {
    const val DEFAULT_FREEDOM_URL = "https://api.freedompay.kz/"
    const val RU_PAYBOX_URL = "https://api.paybox.ru/"
    const val UZ_FREEDOM_URL = "https://api.freedompay.uz/"
    const val KG_FREEDOM_URL = "https://api.freedompay.kg/"
    const val CUSTOMER_DEFAULT_URL = "https://customer.freedompay.kz"
    const val CUSTOMER_RU_URL = "https://customer.paybox.ru"
    const val CUSTOMER_UZ_URL = "https://customer.freedompay.uz"
    const val CUSTOMER_KG_URL = "https://customer.freedompay.kg"


    const val PAY_HTML = "pay.html"
    const val ABOUT_BLANK = "about:blank"
    const val CARDSTORAGE = "/cardstorage/"
    const val CARD = "/card/"
    const val PAY_ROUTE = "/pay/"
    const val LISTCARD_URL = "list"
    const val CARDINITPAY = "init"
    const val ADDCARD_URL = "add"
    const val PAY = "pay"
    const val REMOVECARD_URL = "remove"
    const val DIRECT = "direct"

    const val SAMSUNG_PAY_URL = "samsungpay://"

    var region: Region = DEFAULT

    fun getBaseUrl(): String =
        when (region) {
            DEFAULT -> DEFAULT_FREEDOM_URL
            UZ -> UZ_FREEDOM_URL
            RU -> RU_PAYBOX_URL
            KG -> KG_FREEDOM_URL
        }

    fun getCustomerUrl(): String =
        when (region) {
            DEFAULT -> CUSTOMER_DEFAULT_URL
            UZ -> CUSTOMER_UZ_URL
            RU -> CUSTOMER_RU_URL
            KG -> CUSTOMER_KG_URL
        }

    fun getCustomerDomain(): String =
        when (region) {
            DEFAULT -> CUSTOMER_DEFAULT_URL
            UZ -> CUSTOMER_UZ_URL
            RU -> CUSTOMER_RU_URL
            KG -> CUSTOMER_KG_URL
        }.replace("https://", "")

    fun statusUrl() = "${getBaseUrl()}get_status.php"
    fun initPaymentUrl() = "${getBaseUrl()}init_payment.php"
    fun revokeUrl() = "${getBaseUrl()}revoke.php"
    fun cancelUrl() = "${getBaseUrl()}cancel.php"
    fun clearingUrl() = "${getBaseUrl()}do_capture.php"
    fun recurringUrl() = "${getBaseUrl()}make_recurring_payment.php"
    fun successUrl() = "${getBaseUrl()}success"
    fun failureUrl() = "${getBaseUrl()}failure"

    fun cardPay(merchant_id: String): String {
        return "${getBaseUrl()}v1/merchant/${merchant_id}${CARD}"
    }

    fun cardMerchant(merchant_id: String): String {
        return "${getBaseUrl()}v1/merchant/${merchant_id}${CARDSTORAGE}"
    }

    fun nonAcceptanceDirect(merchant_id: String): String {
        return "${DEFAULT_FREEDOM_URL}v1/merchant/${merchant_id}${CARD}${DIRECT}"
    }

    fun confirmGooglePayUrl(paymentId: String): String {
        return getCustomerUrl() + PAY_ROUTE + paymentId + "/$PAY"
    }
}

object Params {
    const val RECURRING_PROFILE_ID = "pg_recurring_profile_id"
    const val CARD_CREATED_AT = "created_at"
    const val RESPONSE = "response"
    const val PAYMENT_FAILURE = "Не удалось оплатить"
    const val UNKNOWN_ERROR = "Неизвестная ошибка"
    const val CONNECTION_ERROR = "Ошибка подключения"
    const val FORMAT_ERROR = "Неправильный формат ответа"
    const val RECURRING_PROFILE_EXPIRY = "pg_recurring_profile_expiry_date"
    const val CLEARING_AMOUNT = "pg_clearing_amount"
    const val REFUND_AMOUNT = "pg_refund_amount"
    const val ERROR_CODE = "pg_error_code"
    const val ERROR_DESCRIPTION = "pg_error_description"
    const val CAPTURED = "pg_captured"
    const val CARD_PAN = "pg_card_pan"
    const val CREATED_AT = "pg_create_date"
    const val TRANSACTION_STATUS = "pg_transaction_status"
    const val CAN_REJECT = "pg_can_reject"
    const val REDIRECT_URL = "pg_redirect_url"
    const val MERCHANT_ID = "pg_merchant_id"
    const val SIG = "pg_sig"
    const val SALT = "pg_salt"
    const val STATUS = "pg_status"
    const val CARD_ID = "pg_card_id"
    const val CARD_HASH = "pg_card_hash"
    const val TEST_MODE = "pg_testing_mode"
    const val RECURRING_START = "pg_recurring_start"
    const val AUTOCLEARING = "pg_auto_clearing"
    const val REQUEST_METHOD = "pg_request_method"
    const val CURRENCY = "pg_currency"
    const val LIFETIME = "pg_lifetime"
    const val ENCODING = "pg_encoding"
    const val RECURRING_LIFETIME = "pg_recurring_lifetime"
    const val PAYMENT_SYSTEM = "pg_payment_system"
    const val SUCCESS_METHOD = "pg_success_url_method"
    const val FAILURE_METHOD = "pg_failure_url_method"
    const val SUCCESS_URL = "pg_success_url"
    const val FAILURE_URL = "pg_failure_url"
    const val BACK_LINK = "pg_back_link"
    const val POST_LINK = "pg_post_link"
    const val LANGUAGE = "pg_language"
    const val USER_PHONE = "pg_user_phone"
    const val USER_CONTACT_EMAIL = "pg_user_contact_email"
    const val USER_EMAIL = "pg_user_email"
    const val CAPTURE_URL = "pg_capture_url"
    const val REFUND_URL = "pg_refund_url"
    const val RESULT_URL = "pg_result_url"
    const val CHECK_URL = "pg_check_url"
    const val USER_ID = "pg_user_id"
    const val ORDER_ID = "pg_order_id"
    const val DESCRIPTION = "pg_description"
    const val RECURRING_PROFILE = "pg_recurring_profile"
    const val AMOUNT = "pg_amount"
    const val PAYMENT_ID = "pg_payment_id"
    const val CUSTOMER = "customer"
    const val TIMEOUT_AFTER_PAYMENT = "pg_timeout_after_payment"
    const val PAYMENT_ROUTE = "pg_payment_route"
    const val CARD = "card"
    const val ERROR = "error"
    const val NONE = "NONE"
    const val FRAME = "frame"
    const val GET = "GET"
    const val GOOGLE_PAY = "google_pay"
    const val CARD_TOKEN = "pg_card_token"
    const val TYPE = "type"
    const val PAYMENTSYSTEM = "paymentSystem"
    const val TOKEN = "token"
    const val URL = "url"
    const val STATUS_JSON = "status"
    const val CODE = "code"
    const val MESSAGE = "message"
    const val DATA = "data"
    const val BACK_URL = "back_url"
    const val PARAMS = "params"
}