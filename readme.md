
**Paybox SDK (Android, kotlin)**

PayBox SDK Android - это библиотека позволяющая упростить взаимодействие с API PayBox. Система SDK работает на Android 4.4 и выше

**Описание возможностей:**

- Инициализация платежа
- Отмена платежа
- Возврат платежа
- Проведение клиринга
- Проведение рекуррентного платежа с сохраненными картами
- Получение информации/статуса платежа
- Добавление карт/Удаление карт
- Оплата добавленными картами

**Установка:**

1. Добавьте payboxsdk.aar в ваш проект.


**Работа с SDK**

*Инициализация SDK:*

```
	var sdk = PayboxSdk.initialize(merchantID, "secretKey")
```

Добавьте PaymentView в ваше activity:

 ```
 	<money.paybox.payboxsdk.view.PaymentView
            android:id="@+id/paymentView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
 ```

Передайте экземпляр paymentView в sdk:

```
	sdk.setPaymentView(paymentView)
```

Для отслеживания прогресса загрузки платежной страницы используйте WebListener:
```
	paymentView.listener = this
        
    override fun onLoadStarted() {
                
    }  
    override fun onLoadFinished() {
                
    }    
```

*Создание платежа:*

```
    sdk.createPayment(amount, "description", "orderId", userId, extraParams) {
            payment, error ->   //Вызовется после оплаты
    }
```
После вызова в paymentView откроется платежная страница


*Рекурентный платеж:*
```
   sdk.createRecurringPayment(amount,"description","recurringProfile", "orderId") {
            recurringPayment, error -> //Вызовется после оплаты
   }
```

*Получение статуса платежа:*
```
   sdk.getPaymentStatus(paymentId) {
            status, error ->  // Вызовется после получения ответа
   }
```

*Клиринг платежа:*
```
   sdk.makeClearingPayment(paymentId, amount) {     // Если указать null вместо суммы клиринга, то клиринг пройдет на всю сумму платежа
            capture, error -> // Вызовется после клиринга
   }
```

*Отмена платежа:*
```
   sdk.makeCancelPayment(paymentId) {
            payment, error -> //Вызовется после отмены
   }
```

*Возврат платежа:*
```
   sdk.makeRevokePayment(paymentId, amount) {
            payment, error -> //Вызовется после возврата
   }
```

*Сохранение карты:*
```
   sdk.addNewCard(userId,"postLink") {
            payment, error -> // Вызовется после добавления
   }
```
После вызова в paymentView откроется платежная страница

*Получить список сохраненых карт:*
```
   sdk.getAddedCards(userId){
            cards, error -> // Вызовется после получения ответа
   }
```

*Удаление сохраненой карты:*
```
   sdk.removeAddedCard(cardId, userId) {
            card, error ->  // Вызовется после получения ответа
   }
```

*Создание платежа сохраненой картой:*
```
   sdk.createCardPayment(amount, userId, cardId, "description", "orderId"){
            payment, error -> // Вызовется после создания
   }
```
Для оплаты созданного платежа:
```
   sdk.payByCard(paymentId){
            payment, error -> //Вызовется после оплаты
   }
```
После вызова в paymentView откроется платежная страница для 3ds аутентификации


**Настройки SDK**

*Тестовый режим:*
```
    sdk.config().testMode(enabled)  //По умолчанию тестовый режим включен
```

*Выбор платежной системы:*
```
    sdk.config().setPaymentSystem(paymentSystem)
```

*Выбор валюты платежа:*
```
    sdk.config().setCurrencyCode(code)
```

*Активация автоклиринга:*
```
    sdk.config().autoClearing(enabled)
```

*Установка кодировки:*
```
    sdk.config().setEncoding(encoding) //по умолчанию UTF-8
```

*Время жизни рекурентного профиля:*
```
    sdk.config().setRecurringLifetime(lifetime) //по умолчанию 36 месяцев
```

*Время жизни платежной страницы, в течение которого платеж должен быть завершен:*
```
    sdk.config().setPaymentLifetime(lifetime)  //по умолчанию 300 секунд
```

*Включение режима рекурентного платежа:*
```
    sdk.config().recurringMode(enabled)  //по умолчанию отключен
```

*Номер телефона клиента, будет отображаться на платежной странице. Если не указать, то будет предложено ввести на платежной странице:*
```
    sdk.config().setUserPhone(userPhone)
```

*Email клиента, будет отображаться на платежной странице. Если не указать email, то будет предложено ввести на платежной странице:*
```
    sdk.config().setUserEmail(userEmail)
```

*Язык платежной страницы:*
```
    sdk.config().setLanguage(language)
```

*Для передачи информации от платежного гейта:*
```
    sdk.config().setCheckUrl(url)
    sdk.config().setResultUrl(url)
    sdk.config().setRefundUrl(url)
    sdk.config().setClearingUrl(url)
    sdk.config().setRequestMethod(requestMethod)
```

