package money.paybox.payboxsdk.view

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import money.paybox.payboxsdk.interfaces.WebListener

class PaymentView : FrameLayout {

    private lateinit var webView: WebView
    private var sOf: ((isSuccess: Boolean) -> Unit)? = null
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    init {
        initWebview()
    }

    fun loadPaymentPage(url: String, sucessOrFailure: (isSuccess: Boolean) -> Unit) {
        if (url.startsWith("https://api.paybox.money") || url.startsWith("https://customer.paybox.money")) {
            this.webView.loadUrl(url)
            this.sOf = sucessOrFailure
        }
    }
    var listener: WebListener? = null

    fun initWebview() {
        webView = WebView(context)
        addView(webView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true
        webView.webViewClient = object: WebViewClient() {

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
                when {
                    url.contains("success") -> {
                        callSdk(url)
                        view?.loadUrl("about:blank")
                    }
                    url.contains("failure") -> {
                        callSdk(url)
                        view?.loadUrl("about:blank")
                    }
                    else -> view?.loadUrl(url)
                }
                return true
            }
        }
    }

    private fun callSdk(url: String){
        sOf?.let {
            it(url.contains("success"))
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
        if (this.childCount>=1) {
            return
        }
        super.addView(child)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (this.childCount>=1) {
            return
        }
        super.addView(child, index, params)
    }

    override fun addView(child: View?, index: Int) {
        if (this.childCount>=1) {
            return
        }
        super.addView(child, index)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (this.childCount>=1) {
            return
        }
        super.addView(child, params)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        if (this.childCount>=1) {
            return
        }
        super.addView(child, width, height)
    }

    override fun addViewInLayout(child: View?, index: Int, params: ViewGroup.LayoutParams?): Boolean {
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
