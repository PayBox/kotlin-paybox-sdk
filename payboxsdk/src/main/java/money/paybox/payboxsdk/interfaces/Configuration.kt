package money.paybox.payboxsdk.interfaces

import money.paybox.payboxsdk.config.Language
import money.paybox.payboxsdk.config.PaymentSystem
import money.paybox.payboxsdk.config.Region
import money.paybox.payboxsdk.config.RequestMethod

/**
 * Настройки Sdk
 */
interface Configuration {

    /**
     * Установка номера телефона клиента, будет отображаться на платежной странице. Если не указать, то будет предложено ввести на платежной странице
     * @return
     * @param userPhone номер телефона клиента
     */
    fun setUserPhone(userPhone: String)

    /**
     * Установка email клиента, будет отображаться на платежной странице. Если не указать email, то будет предложено ввести на платежной странице
     * @return
     * @param userEmail email клиента
     */
    fun setUserEmail(userEmail: String)

    /**
     * Установка тестового режима
     * @return
     * @param enabled true или false
     */
    fun testMode(enabled: Boolean)

    /**
     * Установка платежной системы
     * @return
     * @param paymentSystem платежная система
     */
    fun setPaymentSystem(paymentSystem: PaymentSystem)

    /**
     * Установка метода вызова сервиса мерчанта, для обращения от системы Paybox к системе мерчанта по ссылкам checkUrl, resultUrl, refundUrl, clearingUrl
     * @return
     * @param requestMethod метод запроса
     */
    fun setRequestMethod(requestMethod: RequestMethod)

    /**
     * Установка языка платежной страницы
     * @return
     * @param language язык
     */
    fun setLanguage(language: Language)

    /**
     * Установка автоклиринга платежей
     * @return
     * @param enabled true или false
     */
    fun autoClearing(enabled: Boolean)

    /**
     * Установка кодировки
     * @return
     * @param encoding кодировка, по умолчанию UTF-8
     */
    fun setEncoding(encoding: String)

    /**
     * Установка времени жизни рекурентного профиля
     * @return
     * @param lifetime время жизни (в месяцах), по умолчанию 36 месяцев
     */
    fun setRecurringLifetime(lifetime: Int)

    /**
     * Установка времени жизни платежной страницы, в течение которого платеж должен быть завершен
     * @return
     * @param lifetime время жизни (в секундах), по умолчанию 300 секунд
     */
    fun setPaymentLifetime(lifetime: Int)

    /**
     * Установка включения режима рекурентного платежа
     * @return
     * @param enabled true или false, по умолчанию false
     */
    fun recurringMode(enabled: Boolean)

    /**
     * Установка ссылки на сервис мерчанта, для проверки возможности платежа. Вызывается перед платежом, если платежная система предоставляет такую возможность
     * @return
     * @param url ссылка
     */
    fun setCheckUrl(url: String)

    /**
     * Установка ссылки на сервис мерчанта, для сообщения о результате платежа. Вызывается после платежа в случае успеха или неудачи
     * @return
     * @param url ссылка
     */
    fun setResultUrl(url: String)

    /**
     * Установка ссылки на сервис мерчанта, для сообщения о результате платежа. для сообщения об отмене платежа. Вызывается после платежа в случае отмены платежа на стороне PayBoxа или ПС
     * @return
     * @param url ссылка
     */
    fun setRefundUrl(url: String)

    /**
     * Установка ссылки на сервис мерчанта, для сообщения о проведении клиринга платежа по банковской карте
     * @return
     * @param url ссылка
     */
    fun setClearingUrl(url: String)

    /**
     * Установка кода валюты, в которой указана сумма
     * @return
     * @param code код вылюты, пример: KZT, USD, EUR
     */
    fun setCurrencyCode(code: String)

    /**
     * Установка фрейма вместо платежной страницы при оплате
     * @return
     * @param isFrameRequired значение true если необходимо отображать фрейм вместо платежной страницы
     */
    fun setFrameRequired(isFrameRequired: Boolean)

    /**
     * Установка региона
     * @return
     * @param region Регион
     */
    fun setRegion(region: Region)
}