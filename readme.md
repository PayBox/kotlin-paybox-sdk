
**Paybox SDK (Android, kotlin)**

PayBox SDK Android - это библиотека позволяющая упростить взаимодействие с API PayBox. Система SDK работает на Android 4.4 и выше

[Скачать демонстрационный APK](https://github.com/PayBox/sample-android-kotlin-sdk/raw/master/sample-kotlin-sdk.apk)

[Исходный код демонстрационного приложения](https://github.com/PayBox/sample-android-kotlin-sdk)

<img src="https://github.com/PayBox/sample-android-kotlin-sdk/raw/master/kotlin_init_pay.gif" width="25%" height="25%"/>

**Описание возможностей:**

- Инициализация платежа
- Отмена платежа
- Возврат платежа
- Проведение клиринга
- Проведение рекуррентного платежа с сохраненными картами
- Получение информации/статуса платежа
- Добавление карт/Удаление карт
- Оплата добавленными картами

# **Установка:**

1. Добавьте репозитории Jitpack в ваш build.gradle на уровне проекта в конец репозиториев allprojects:
```
allprojects {
    repositories {
        // ...
        maven { url "https://jitpack.io" }
    }
}
```

2.Добавьте в ваш build.gradle:
```
dependencies {
	implementation 'com.github.PayBox:kotlin-paybox-sdk:0.9.11'
}
```
---

# Для связи с SDK
1. Инициализация SDK:
```
	var sdk = PayboxSdk.initialize(merchantID, "secretKey")
```

2. Добавьте PaymentView в ваше activity:
 ```
 	<money.paybox.payboxsdk.view.PaymentView
            android:id="@+id/paymentView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
 ```

3. Передайте экземпляр paymentView в sdk:
```
	sdk.setPaymentView(paymentView)
```

4. Для отслеживания прогресса загрузки платежной страницы используйте WebListener:
```
    paymentView.listener = this
        
    override fun onLoadStarted() {
                
    }
    
    override fun onLoadFinished() {
                
    }    
```
---

# **Настройки SDK**

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

*Для выбора Frame вместо платежной страницы:*
```
    sdk.config().setFrameRequired(true) //false по умолчанию
```
---

# **Работа с SDK**

## *Создание платежа:*

```
    sdk.createPayment(amount, "description", "orderId", userId, extraParams) {
            payment, error ->   //Вызовется после оплаты
    }
```
После вызова в paymentView откроется платежная страница


## *Рекурентный платеж:*
```
   sdk.createRecurringPayment(amount,"description","recurringProfile", "orderId") {
            recurringPayment, error -> //Вызовется после оплаты
   }
```

## *Получение статуса платежа:*
```
   sdk.getPaymentStatus(paymentId) {
            status, error ->  // Вызовется после получения ответа
   }
```

## *Клиринг платежа:*
```
   sdk.makeClearingPayment(paymentId, amount) {     // Если указать null вместо суммы клиринга, то клиринг пройдет на всю сумму платежа
            capture, error -> // Вызовется после клиринга
   }
```

## *Отмена платежа:*
```
   sdk.makeCancelPayment(paymentId) {
            payment, error -> //Вызовется после отмены
   }
```

## *Возврат платежа:*
```
   sdk.makeRevokePayment(paymentId, amount) {
            payment, error -> //Вызовется после возврата
   }
```

## *Сохранение карты:*
```
   sdk.addNewCard(userId,"postLink") {
            payment, error -> // Вызовется после добавления
   }
```
После вызова в paymentView откроется платежная страница

## *Получить список сохраненых карт:*
```
   sdk.getAddedCards(userId){
            cards, error -> // Вызовется после получения ответа
   }
```

## *Удаление сохраненой карты:*
```
   sdk.removeAddedCard(cardId, userId) {
            card, error ->  // Вызовется после получения ответа
   }
```

## *Создание платежа сохраненой картой:*
```
   sdk.createCardPayment(amount, userId, cardId, "description", "orderId"){
            payment, error -> // Вызовется после создания
   }
```
## *Для оплаты созданного платежа:*
```
   sdk.payByCard(paymentId){
            payment, error -> //Вызовется после оплаты
   }
```
После вызова в paymentView откроется платежная страница для 3ds аутентификации
