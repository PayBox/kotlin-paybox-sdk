package money.paybox.payboxsdk.interfaces

import money.paybox.payboxsdk.view.PaymentView
import money.paybox.payboxsdk.models.RecurringPayment
import money.paybox.payboxsdk.models.Capture
import money.paybox.payboxsdk.models.Card
import money.paybox.payboxsdk.models.Error
import money.paybox.payboxsdk.models.Payment
import money.paybox.payboxsdk.models.Status


interface PayboxSdkInterface {

    /**
     * Передайте сюда paymentView добавленный в ваш activity
     * @return
     * @param paymentView webView на котором будет открываться платежнвя страница
     */
    fun setPaymentView(paymentView: PaymentView)

    /**
     * Создание нового платежа
     * @return
     * @param amount сумма платежа
     * @param description комментарии, описание платежа
     * @param orderId ID заказа платежа
     * @param userId ID пользователя в системе мерчанта
     * @param extraParams доп. параметры мерчанта
     * @param paymentPaid callback от Api Paybox
     */
    fun createPayment(
        amount: Float,
        description: String,
        orderId: String? = null,
        userId: String? = null,
        extraParams: HashMap<String, String>? = null,
        paymentPaid: (payment: Payment?, error: Error?) -> Unit
    )

    /**
     * Создание рекурентного платежа
     * @return
     * @param amount сумма платежа
     * @param description комментарий, описание платежа
     * @param orderId ID заказа платежа
     * @param recurringProfile рекурентный профиль в системе Paybox
     * @param extraParams доп. параметры мерчанта
     * @param recurringPaid callback от Api Paybox
     */
    fun createRecurringPayment(
        amount: Float,
        description: String,
        recurringProfile: String,
        orderId: String? = null,
        extraParams: HashMap<String, String>? = null,
        recurringPaid: (recurringPayment: RecurringPayment?, error: Error?) -> Unit
    )

    /**
     * Создание платежа добавленной картой
     * @return
     * @param amount сумма платежа
     * @param description комментарии, описание платежа
     * @param orderId ID заказа платежа
     * @param userId ID пользователя в системе мерчанта
     * @param cardId ID сохраненной карты в системе Paybox
     * @param extraParams доп. параметры мерчанта
     * @param payCreated callback от Api Paybox
     */
    @Deprecated(
        "This method has deprecated cardId parameter. Use createCardPayment method with cardToken parameter",
        ReplaceWith(
            "this.createCardPayment(amount, userId, cardToken, description, orderId, extraParams, payCreated)",
            "money.paybox.payboxsdk.interfaces"
        ), DeprecationLevel.WARNING
    )
    fun createCardPayment(
        amount: Float,
        userId: String,
        cardId: Int,
        description: String,
        orderId: String,
        extraParams: HashMap<String, String>? = null,
        payCreated: (payment: Payment?, error: Error?) -> Unit
    )

    /**
     * Создание платежа добавленной картой
     * @return
     * @param amount сумма платежа
     * @param description комментарии, описание платежа
     * @param orderId ID заказа платежа
     * @param userId ID пользователя в системе мерчанта
     * @param cardToken Token сохраненной карты в системе Paybox
     * @param extraParams доп. параметры мерчанта
     * @param payCreated callback от Api Paybox
     */
    fun createCardPayment(
        amount: Float,
        userId: String,
        cardToken: String,
        description: String,
        orderId: String,
        extraParams: HashMap<String, String>? = null,
        payCreated: (payment: Payment?, error: Error?) -> Unit
    )

    /**
     * Оплата созданного платежа, добавленной картой
     * @return
     * @param paymentId ID платежа в системе Paybox
     * @param paymentPaid callback от Api Paybox
     */
    fun payByCard(paymentId: Int, paymentPaid: (payment: Payment?, error: Error?) -> Unit)

    /**
     * Получить статус платежа
     * @return
     * @param paymentId ID платежа в системе Paybox
     * @param status callback от Api Paybox
     */
    fun getPaymentStatus(paymentId: Int, status: (status: Status?, error: Error?) -> Unit)

    /**
     * Провести возврат платежа
     * @return
     * @param paymentId ID платежа в системе Paybox
     * @param amount сумма платежа
     * @param revoked callback от Api Paybox
     */
    fun makeRevokePayment(
        paymentId: Int,
        amount: Float,
        revoked: (payment: Payment?, error: Error?) -> Unit
    )

    /**
     * Провести клиринг платежа
     * @return
     * @param paymentId ID платежа в системе Paybox
     * @param amount сумма платежа
     * @param cleared callback от Api Paybox
     */
    fun makeClearingPayment(
        paymentId: Int,
        amount: Float? = null,
        cleared: (capture: Capture?, error: Error?) -> Unit
    )

    /**
     * Провести отмену платежа (отмена платежа проводится до клиринга)
     * @return
     * @param paymentId ID платежа в системе Paybox
     * @param canceled callback от Api Paybox
     */
    fun makeCancelPayment(paymentId: Int, canceled: (payment: Payment?, error: Error?) -> Unit)

    /**
     * Сохранение новой карты в системе Paybox
     * @return
     * @param userId ID пользователя в системе мерчанта
     * @param postLink ссылка на сервис мерчанта, будет вызван после сохранения карты
     * @param cardAdded callback от Api Paybox
     */
    fun addNewCard(
        userId: String,
        postLink: String? = null,
        cardAdded: (payment: Payment?, error: Error?) -> Unit
    )

    /**
     * Удаление сохраненой карты
     * @return
     * @param cardId ID сохраненной карты в системе Paybox
     * @param userId ID пользователя в системе мерчанта
     * @param removed callback от Api Paybox
     */
    fun removeAddedCard(cardId: Int, userId: String, removed: (card: Card?, error: Error?) -> Unit)

    /**
     * Получить список сохраненых карт
     * @returт
     * @param userId ID пользователя в системе мерчанта
     * @param cardList callback от Api Paybox
     */
    fun getAddedCards(userId: String, cardList: (cards: ArrayList<Card>?, error: Error?) -> Unit)

    /**
     * Настройки Sdk
     * @return
     */
    fun config(): Configuration

    /**
     * Проведение безакцептного списания
     * @return
     * @param paymentId ID платежа в системе Paybox
     * @param paymentPaid callback от Api Paybox
     */
    fun createNonAcceptancePayment(
        paymentId: Int?,
        paymentPaid: (payment: Payment?, error: Error?) -> Unit
    )
    /**
     * Создание нового платежа через Google Pay
     * @return
     * @param amount сумма платежа
     * @param description комментарии, описание платежа
     * @param orderId ID заказа платежа
     * @param userId ID пользователя в системе мерчанта
     * @param extraParams доп. параметры мерчанта
     * @param paymentPaid callback от Api Paybox
     */
    fun createGooglePayment(
        amount: Float,
        description: String,
        orderId: String?,
        userId: String?,
        extraParams: HashMap<String, String>?,
        paymentPaid: (paymentId: String?, error: Error?) -> Unit
    )
    /**
     * Подтверждение платежа
     * @return
     * @param paymentId идентификатор платежа полученный из запроса на инициализацию платежа [createGooglePayment].
     * @param token  токен от Google Pay
     * @param paymentPaid callback от Api Paybox
     */
    fun confirmGooglePayment(
        paymentId: String,
        token: String,
        paymentPaid: (payment: Payment?, error: Error?) -> Unit
    )
}