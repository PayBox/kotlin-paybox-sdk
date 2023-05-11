package money.paybox.payboxsdk

import money.paybox.payboxsdk.api.Signing
import money.paybox.payboxsdk.api.ApiHelper
import money.paybox.payboxsdk.api.Params
import money.paybox.payboxsdk.api.Urls
import money.paybox.payboxsdk.config.ConfigurationImp
import money.paybox.payboxsdk.interfaces.ApiListener
import money.paybox.payboxsdk.interfaces.Configuration
import money.paybox.payboxsdk.interfaces.PayboxSdkInterface
import money.paybox.payboxsdk.models.*
import money.paybox.payboxsdk.view.PaymentView
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PayboxSdk() : PayboxSdkInterface, ApiListener, Signing() {

    private lateinit var configs: ConfigurationImp
    private lateinit var helper: ApiHelper
    private var paymentView: WeakReference<PaymentView>? = null
    private var paymentPaidReference: ((payment: Payment?, error: Error?) -> Unit)? = null
    private var cardAddingReference: ((payment: Payment?, error: Error?) -> Unit)? = null
    private var canceledReference: ((payment: Payment?, error: Error?) -> Unit)? = null
    private var revokedReference: ((payment: Payment?, error: Error?) -> Unit)? = null
    private var cardPayInitReference: ((payment: Payment?, error: Error?) -> Unit)? = null
    private var captureReference: ((capture: Capture?, error: Error?) -> Unit)? = null
    private var statusReference: ((status: Status?, error: Error?) -> Unit)? = null
    private var cardListReference: ((cards: ArrayList<Card>?, error: Error?) -> Unit)? = null
    private var cardRemovedReference: ((card: Card?, error: Error?) -> Unit)? = null
    private var recurringPaidReference: ((recurringPayment: RecurringPayment?, error: Error?) -> Unit)? =
        null
    override lateinit var secretKey: String

    private constructor(merchantId: Int, secretKey: String) : this() {
        this.configs = ConfigurationImp(merchantId)
        this.helper = ApiHelper(this, secretKey)
        this.secretKey = secretKey
    }


    companion object {
        /**
         * Инициализация SDK
         * @param merchantId ID мерчанта в системе Paybox
         * @param secretKey секретный ключ мерчанта в системе Paybox
         */
        fun initialize(merchantId: Int, secretKey: String): PayboxSdkInterface {
            return PayboxSdk(merchantId, secretKey)
        }
    }

    override fun setPaymentView(paymentView: PaymentView) {
        this.paymentView = WeakReference(paymentView)
    }

    override fun createPayment(
        amount: Float,
        description: String,
        orderId: String?,
        userId: String?,
        extraParams: HashMap<String, String>?,
        paymentPaid: (payment: Payment?, error: Error?) -> Unit
    ) {
        this.paymentPaidReference = paymentPaid
        val params = configs.getParams(extraParams)
        orderId?.let {
            params[Params.ORDER_ID] = it
        }
        userId?.let {
            params[Params.USER_ID] = userId
        }
        params[Params.AMOUNT] = amount.toString()
        params[Params.DESCRIPTION] = description
        helper.initConnection(Urls.INIT_PAYMENT_URL, params)
    }

    override fun createRecurringPayment(
        amount: Float,
        description: String,
        recurringProfile: String,
        orderId: String?,
        extraParams: HashMap<String, String>?,
        recurringPaid: (recurringPayment: RecurringPayment?, error: Error?) -> Unit
    ) {
        this.recurringPaidReference = recurringPaid
        val params = configs.getParams(extraParams)
        orderId?.let {
            params[Params.ORDER_ID] = it
        }
        params[Params.AMOUNT] = amount.toString()
        params[Params.DESCRIPTION] = description
        params[Params.RECURRING_PROFILE] = recurringProfile
        helper.initConnection(Urls.RECURRING_URL, params)
    }

    override fun getPaymentStatus(
        paymentId: Int,
        status: (status: Status?, error: Error?) -> Unit
    ) {
        this.statusReference = status
        val params = configs.getParams()
        params[Params.PAYMENT_ID] = paymentId.toString()
        helper.initConnection(Urls.STATUS_URL, params)

    }

    override fun makeRevokePayment(
        paymentId: Int,
        amount: Float,
        revoked: (payment: Payment?, error: Error?) -> Unit
    ) {
        revokedReference = revoked
        val params = configs.getParams()
        params[Params.PAYMENT_ID] = paymentId.toString()
        params[Params.REFUND_AMOUNT] = amount.toString()
        helper.initConnection(Urls.REVOKE_URL, params)
    }

    override fun makeClearingPayment(
        paymentId: Int,
        amount: Float?,
        cleared: (capture: Capture?, error: Error?) -> Unit
    ) {
        this.captureReference = cleared
        val params = configs.getParams()
        params[Params.PAYMENT_ID] = paymentId.toString()
        amount?.let {
            params[Params.CLEARING_AMOUNT] = it.toString()
        }
        helper.initConnection(Urls.CLEARING_URL, params)
    }

    override fun makeCancelPayment(
        paymentId: Int,
        canceled: (payment: Payment?, error: Error?) -> Unit
    ) {
        this.canceledReference = canceled
        val params = configs.getParams()
        params[Params.PAYMENT_ID] = paymentId.toString()
        helper.initConnection(Urls.CANCEL_URL, params)
    }

    override fun addNewCard(
        userId: String,
        postLink: String?,
        cardAdded: (payment: Payment?, error: Error?) -> Unit
    ) {
        this.cardAddingReference = cardAdded
        val params = configs.getParams()
        params[Params.USER_ID] = userId
        postLink?.let {
            params[Params.POST_LINK] = it
        }
        helper.initConnection(
            Urls.CARD_MERCHANT(configs.merchantId.toString()) + Urls.ADDCARD_URL,
            params
        )
    }

    override fun removeAddedCard(
        cardId: Int,
        userId: String,
        removed: (card: Card?, error: Error?) -> Unit
    ) {
        this.cardRemovedReference = removed
        val params = configs.getParams()
        params[Params.CARD_ID] = cardId.toString()
        params[Params.USER_ID] = userId
        helper.initConnection(
            Urls.CARD_MERCHANT(configs.merchantId.toString()) + Urls.REMOVECARD_URL,
            params
        )
    }

    override fun getAddedCards(
        userId: String,
        cardList: (cards: ArrayList<Card>?, error: Error?) -> Unit
    ) {
        this.cardListReference = cardList
        val params = configs.getParams()
        params[Params.USER_ID] = userId
        helper.initConnection(
            Urls.CARD_MERCHANT(configs.merchantId.toString()) + Urls.LISTCARD_URL,
            params
        )
    }

    override fun payByCard(
        paymentId: Int,
        paymentPaid: (payment: Payment?, error: Error?) -> Unit
    ) {
        val params = configs.defParams()
        params[Params.PAYMENT_ID] = paymentId.toString()
        var url = Urls.CARD_PAY(configs.merchantId.toString()).plus(Urls.PAY).plus("?")
        params.signedParams(Urls.PAY).forEach {
            url += "${it.key}=${it.value}&"
        }
        paymentView?.get()?.loadPaymentPage(url) { success ->
            if (success) {
                paymentPaid(Payment("success", null, null), null)
            } else {
                paymentPaid(null, Error(10, Params.PAYMENT_FAILURE))
            }
        }
    }


    override fun createCardPayment(
        amount: Float,
        userId: String,
        cardId: Int,
        description: String,
        orderId: String,
        extraParams: HashMap<String, String>?,
        payCreated: (payment: Payment?, error: Error?) -> Unit
    ) {
        cardPayInitReference = payCreated
        val params = configs.getParams(extraParams)
        params[Params.ORDER_ID] = orderId
        params[Params.AMOUNT] = amount.toString()
        params[Params.USER_ID] = userId
        params[Params.CARD_ID] = cardId.toString()
        params[Params.DESCRIPTION] = description
        helper.initConnection(
            Urls.CARD_PAY(configs.merchantId.toString()) + Urls.CARDINITPAY,
            params
        )
    }

    override fun config(): Configuration {
        return configs
    }

    override fun onPaymentInited(payment: Payment?, error: Error?) {
        if (payment != null) {
            payment.redirectUrl?.let {
                paymentView?.get()?.loadPaymentPage(it) { success ->
                    paymentPaidReference?.let {
                        if (success) {
                            it(payment, null)
                        } else {
                            it(null, Error(10, Params.PAYMENT_FAILURE))
                        }
                    }
                }
            }
        } else {
            paymentPaidReference?.let {
                it(null, error)
            }
        }
    }

    override fun onPaymentRevoked(payment: Payment?, error: Error?) {
        revokedReference?.let {
            it(payment, error)
        }
    }

    override fun onPaymentCanceled(payment: Payment?, error: Error?) {
        canceledReference?.let {
            it(payment, error)
        }
    }

    override fun onPaymentStatus(status: Status?, error: Error?) {
        statusReference?.let {
            it(status, error)
        }
    }

    override fun onPaymentRecurring(recurringPayment: RecurringPayment?, error: Error?) {
        recurringPaidReference?.let {
            it(recurringPayment, error)
        }
    }

    override fun onCardAdding(payment: Payment?, error: Error?) {
        if (payment != null) {
            payment.redirectUrl?.let {
                paymentView?.get()?.loadPaymentPage(it) { success ->
                    cardAddingReference?.let {
                        if (success) {
                            it(payment, null)
                        } else {
                            it(null, Error(10, Params.PAYMENT_FAILURE))
                        }

                    }
                }
            }
        } else {
            cardAddingReference?.let {
                it(null, error)
            }
        }
    }

    override fun onCardListing(cards: ArrayList<Card>?, error: Error?) {
        cardListReference?.let {
            it(cards, error)
        }
    }

    override fun onCardRemoved(card: Card?, error: Error?) {
        cardRemovedReference?.let {
            it(card, error)
        }
    }

    override fun onCardPayInited(payment: Payment?, error: Error?) {
        cardPayInitReference?.let {
            it(payment, error)
        }
    }

    override fun onCapture(capture: Capture?, error: Error?) {
        captureReference?.let {
            it(capture, error)
        }
    }
}

