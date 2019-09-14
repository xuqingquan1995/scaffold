package top.xuqingquan.web.publics

import android.os.Handler
import android.os.Looper

class JsAccessEntraceImpl : BaseJsAccessEntrace {

    private val mHandler = Handler(Looper.getMainLooper())

    private constructor(webView: android.webkit.WebView?) : super(webView)

    private constructor(webView: com.tencent.smtt.sdk.WebView?) : super(webView)

    private fun safeCallJs(s: String?, valueCallback: android.webkit.ValueCallback<String>?) {
        mHandler.post { callJs(s, valueCallback) }
    }

    private fun safeCallJs(s: String?, valueCallback: com.tencent.smtt.sdk.ValueCallback<String>?) {
        mHandler.post { callJs(s, valueCallback) }
    }

    override fun callJs(js: String?, callback: android.webkit.ValueCallback<String>?) {
        if (Thread.currentThread() !== Looper.getMainLooper().thread) {
            safeCallJs(js, callback)
            return
        }
        super.callJs(js, callback)
    }

    override fun callJs(js: String?, callback: com.tencent.smtt.sdk.ValueCallback<String>?) {
        if (Thread.currentThread() !== Looper.getMainLooper().thread) {
            safeCallJs(js, callback)
            return
        }
        super.callJs(js, callback)
    }

    companion object {

        @JvmStatic
        fun getInstance(webView: android.webkit.WebView?): JsAccessEntraceImpl {
            return JsAccessEntraceImpl(webView)
        }

        @JvmStatic
        fun getInstance(webView: com.tencent.smtt.sdk.WebView?): JsAccessEntraceImpl {
            return JsAccessEntraceImpl(webView)
        }
    }


}
