package money.paybox.payboxsdk.api

import android.annotation.SuppressLint
import android.os.AsyncTask
import money.paybox.payboxsdk.interfaces.ApiListener
import money.paybox.payboxsdk.models.*
import org.json.JSONObject
import org.json.XML
import java.io.*
import java.net.ConnectException
import java.net.URL
import java.net.URLEncoder
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.*
import javax.net.ssl.HttpsURLConnection

private const val UTF8 = "UTF-8"
private const val TIMEOUT = 25000

abstract class BaseApi : Signing() {

    abstract val listener: ApiListener

    @SuppressLint("StaticFieldLeak")
    fun request(requestData: () -> RequestData) {
        object : AsyncTask<Void, Void, ResponseData>() {
            override fun doInBackground(vararg params: Void?): ResponseData {
                return connection(requestData())
            }

            override fun onPostExecute(result: ResponseData?) {
                resolveResponse(result,requestData().paymentType)
            }
        }.execute()
    }

    private fun connection(requestData: RequestData): ResponseData {
        try {
            val urlCon = URL(requestData.url)
            HttpsURLConnection.setDefaultSSLSocketFactory(TLSSocketFactory())
            val connection = urlCon.openConnection() as HttpsURLConnection
            connection.connectTimeout = TIMEOUT
            connection.requestMethod = requestData.method.name
            connection.useCaches = false
            connection.allowUserInteraction = false
            connection.doInput = true
            connection.doOutput = false
            val stream = connection.outputStream
            val writer = BufferedWriter(
                OutputStreamWriter(stream, UTF8)
            )
            writer.write(makeParams(requestData.params))
            writer.flush()
            writer.close()
            stream.close()
            connection.connect()
            val statusCode = connection.responseCode
            var line = ""
            if (statusCode == HttpsURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val br = BufferedReader(InputStreamReader(inputStream))
                br.readLines().forEach {
                    line += it
                }
                br.close()
                inputStream.close()
            } else {
                val inputStream = connection.errorStream
                val br = BufferedReader(InputStreamReader(inputStream))
                br.readLines().forEach {
                    line += it
                }
                br.close()
                inputStream.close()
            }
            return ResponseData(statusCode, line, requestData.url)
        } catch (e: Exception) {
            if (e is UnknownHostException || e is ConnectException || e is TimeoutException) {
                return ResponseData(522, Params.CONNECTION_ERROR, requestData.url, true)
            }
        }
        return ResponseData(520, Params.UNKNOWN_ERROR, requestData.url, true)
    }

    @Throws(UnsupportedEncodingException::class)
    private fun makeParams(params: SortedMap<String, String>): String {
        val result = StringBuffer()
        var first = true
        for ((key, value) in params) {
            if (first) {
                first = false
            } else {
                result.append("&")
            }
            result.append(URLEncoder.encode(key, UTF8))
            result.append("=")
            result.append(URLEncoder.encode(value, UTF8))
        }
        return result.toString()
    }

    private fun JSONObject.optResponse(key: String): String? {
        if (this.has(Params.RESPONSE)) {
            return this.optJSONObject(Params.RESPONSE)?.optString(key)
        }
        return null
    }

    private fun JSONObject.getPayment(): Payment {
        return Payment(
            status = this.optResponse(Params.STATUS),
            paymentId = this.optResponse(Params.PAYMENT_ID)?.toIntOrNull(),
            merchantId = this.optResponse(Params.MERCHANT_ID)?.toIntOrNull(),
            orderId = this.optResponse(Params.ORDER_ID)?.toIntOrNull(),
            redirectUrl = this.optResponse(Params.REDIRECT_URL)
        )
    }

    private fun JSONObject.getCapture(): Capture {
        return Capture(
            this.optResponse(Params.STATUS),
            this.optResponse(Params.AMOUNT)?.toFloatOrNull(),
            this.optResponse(Params.CLEARING_AMOUNT)?.toFloatOrNull()
        )
    }

    private fun JSONObject.getStatus(): Status {
        return Status(
            this.optResponse(Params.STATUS),
            this.optResponse(Params.PAYMENT_ID)?.toIntOrNull(),
            this.optResponse(Params.TRANSACTION_STATUS),
            this.optResponse(Params.CAN_REJECT),
            this.optResponse(Params.CAPTURED),
            this.optResponse(Params.CARD_PAN),
            this.optResponse(Params.CREATED_AT)
        )
    }

    private fun JSONObject.getCards(): ArrayList<Card> {
        val arrayCard = ArrayList<Card>()
        val arrayCards = this.optJSONObject(Params.RESPONSE)?.optJSONArray(Params.CARD)
        val cardObject = this.optJSONObject(Params.RESPONSE)?.optJSONObject(Params.CARD)
        if (arrayCards != null) {
            for (i in 0 until arrayCards.length()) {
                arrayCard.add(
                    arrayCards.optJSONObject(i).getCard()
                )
            }
        }
        if (cardObject != null) {
            arrayCard.add(cardObject.getCard())
        }
        return arrayCard
    }

    private fun JSONObject.getCard(): Card {
        return Card(
            this.optString(Params.STATUS),
            this.optString(Params.MERCHANT_ID),
            this.optString(Params.CARD_ID),
            this.optString(Params.RECURRING_PROFILE_ID),
            this.optString(Params.CARD_HASH),
            this.optString(Params.CARD_CREATED_AT),
            this.optString(Params.CARD_TOKEN)
        )
    }

    private fun JSONObject.getRecurringPayment(): RecurringPayment {
        return RecurringPayment(
            this.optResponse(Params.STATUS),
            this.optResponse(Params.PAYMENT_ID)?.toIntOrNull(),
            this.optResponse(Params.CURRENCY),
            this.optResponse(Params.AMOUNT)?.toFloatOrNull(),
            this.optResponse(Params.RECURRING_PROFILE),
            this.optResponse(Params.RECURRING_PROFILE_EXPIRY)
        )
    }

    fun resolveResponse(responseData: ResponseData?,paymentType: String?=null) {
        responseData?.let {
            if (!it.error) {
                if (it.response.contains(Params.RESPONSE)) {
                    try {
                        val json = XML.toJSONObject(it.response, true)
                        if (json.optResponse(Params.STATUS) != Params.ERROR) {
                            apiHandler(it.url, json, null,paymentType)
                        } else {
                            handleError(json, it.url)
                        }
                    } catch (e: Exception) {
                        apiHandler(it.url, null, Error(0, Params.FORMAT_ERROR),paymentType)
                    }
                } else {
                    apiHandler(it.url, null, Error(0, Params.FORMAT_ERROR),paymentType)
                }
            } else {
                if (it.response.contains(Params.RESPONSE)) {
                    val json = XML.toJSONObject(it.response, true)
                    handleError(json, it.url)
                } else {
                    apiHandler(it.url, null, Error(it.code, it.response),paymentType)
                }
            }
        }
    }

    private fun handleError(json: JSONObject, url: String) {
        val code = json.optResponse(Params.ERROR_CODE)
        val description = json.optResponse(Params.ERROR_DESCRIPTION)
        apiHandler(
            url, null, Error(
                code?.toInt() ?: 520, description ?: Params.UNKNOWN_ERROR
            )
        )
    }

    private fun apiHandler(url: String, json: JSONObject?, error: Error?,paymentType:String?=null) {
        when {
            url.contains(Urls.initPaymentUrl()) -> {
                if(paymentType ==Params.GOOGLE_PAY) {
                    this.listener.onGooglePayInited(json?.getPayment(), error)
                }else {
                    this.listener.onPaymentInited(json?.getPayment(), error)
                }
            }
            url.contains(Urls.revokeUrl()) -> {
                this.listener.onPaymentRevoked(json?.getPayment(), error)
            }

            url.contains(Urls.cancelUrl()) -> {
                this.listener.onPaymentCanceled(json?.getPayment(), error)
            }

            url.contains(Urls.clearingUrl()) -> {
                this.listener.onCapture(json?.getCapture(), error)
            }

            url.contains(Urls.statusUrl()) -> {
                this.listener.onPaymentStatus(json?.getStatus(), error)
            }

            url.contains(Urls.recurringUrl()) -> {
                this.listener.onPaymentRecurring(json?.getRecurringPayment(), error)
            }

            url.contains(Urls.CARDSTORAGE + Urls.ADDCARD_URL) -> {
                this.listener.onCardAdding(json?.getPayment(), error)
            }

            url.contains(Urls.CARDSTORAGE + Urls.LISTCARD_URL) -> {
                this.listener.onCardListing(json?.getCards(), error)
            }

            url.contains(Urls.CARDSTORAGE + Urls.REMOVECARD_URL) -> {
                this.listener.onCardRemoved(json?.getCards()?.get(0), error)
            }

            url.contains(Urls.CARD + Urls.CARDINITPAY) -> {
                this.listener.onCardPayInited(json?.getPayment(), error)
            }

            url.contains(Urls.CARD + Urls.DIRECT) -> {
                this.listener.onNonAcceptanceDirected(json?.getPayment(), error)
            }
        }
    }
}

