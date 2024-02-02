package money.paybox.payboxsdk.interfaces

import money.paybox.payboxsdk.models.*
import java.util.ArrayList

interface ApiListener {
    fun onPaymentInited(payment: Payment?, error: Error?)
    fun onPaymentRevoked(payment: Payment?, error: Error?)
    fun onPaymentCanceled(payment: Payment?, error: Error?)
    fun onCapture(capture: Capture?, error: Error?)
    fun onPaymentStatus(status: Status?, error: Error?)
    fun onPaymentRecurring(recurringPayment: RecurringPayment?, error: Error?)
    fun onCardAdding(payment: Payment?, error: Error?)
    fun onCardListing(cards: ArrayList<Card>?, error: Error?)
    fun onCardRemoved(card: Card?, error: Error?)
    fun onCardPayInited(payment: Payment?, error: Error?)
    fun onNonAcceptanceDirected(payment: Payment?, error: Error?)
    fun onGooglePayInited(paymentId: String?, error: Error?)
    fun onGooglePayConfirmInited(payment: Payment?, error: Error?)
}