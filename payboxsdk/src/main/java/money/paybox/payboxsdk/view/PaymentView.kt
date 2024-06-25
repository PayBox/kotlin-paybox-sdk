package money.paybox.payboxsdk.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import money.paybox.payboxsdk.api.Urls
import money.paybox.payboxsdk.interfaces.WebListener

class PaymentView : FrameLayout {

    private lateinit var webView: WebView
    private var sOf: ((isSuccess: Boolean) -> Unit)? = null
    private var isFrame = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        attrs
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    init {
        initWebview()
    }

    fun loadPaymentPage(url: String, successOrFailure: (isSuccess: Boolean) -> Unit) {
        if (url.startsWith(Urls.getBaseUrl()) || url.startsWith(Urls.getCustomerUrl())) {
            this.webView.loadUrl(url)
            this.sOf = successOrFailure
            isFrame = !url.contains(Urls.PAY_HTML)
        }
    }

    var listener: WebListener? = null

    private fun initWebview() {
        webView = WebView(context)
        addView(webView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.loadsImagesAutomatically = true
        webSettings.domStorageEnabled = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                listener?.onLoadStarted()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                listener?.onLoadFinished()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url == null) {
                    return false
                }

                if (url.startsWith(Urls.SAMSUNG_PAY_URL)) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        // Samsung Pay app not found
                    }

                    return true
                }

                if (url.startsWith(Urls.successUrl()) || url.startsWith(Urls.failureUrl())) {
                    callSdk(url)
                    view?.loadUrl(Urls.ABOUT_BLANK)
                } else view?.loadUrl(url)

                return true
            }
        }
    }

    private fun callSdk(url: String) {
        if (!isFrame) {
            this@PaymentView.visibility = GONE
        }

        sOf?.let {
            it(url.startsWith(Urls.successUrl()))
        }
    }

    override fun removeAllViews() {
        return
    }

    override fun removeView(view: View?) {
        return
    }

    override fun removeAllViewsInLayout() {
        return
    }

    override fun removeViewAt(index: Int) {
        return
    }

    override fun removeViewsInLayout(start: Int, count: Int) {
        return
    }

    override fun removeViews(start: Int, count: Int) {
        return
    }

    override fun removeViewInLayout(view: View?) {
        return
    }

    override fun addView(child: View?) {
        if (this.childCount >= 1) {
            return
        }
        super.addView(child)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (this.childCount >= 1) {
            return
        }
        super.addView(child, index, params)
    }

    override fun addView(child: View?, index: Int) {
        if (this.childCount >= 1) {
            return
        }
        super.addView(child, index)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (this.childCount >= 1) {
            return
        }
        super.addView(child, params)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        if (this.childCount >= 1) {
            return
        }
        super.addView(child, width, height)
    }

    override fun addViewInLayout(
        child: View?,
        index: Int,
        params: ViewGroup.LayoutParams?
    ): Boolean {
        return true
    }

    override fun addViewInLayout(
        child: View?,
        index: Int,
        params: ViewGroup.LayoutParams?,
        preventRequestLayout: Boolean
    ): Boolean {
        return true
    }

}
