package top.xuqingquan.web.publics

import android.annotation.SuppressLint
import top.xuqingquan.utils.Timber
import top.xuqingquan.web.nokernel.JsBaseInterfaceHolder
import top.xuqingquan.web.nokernel.JsInterfaceHolder
import top.xuqingquan.web.nokernel.JsInterfaceObjectException
import top.xuqingquan.web.nokernel.WebConfig

class JsInterfaceHolderImpl : JsBaseInterfaceHolder {

    private var mWebView: android.webkit.WebView? = null
    private var mx5WebView: com.tencent.smtt.sdk.WebView? = null

    private constructor(webView: android.webkit.WebView?) {
        this.mWebView = webView
    }

    private constructor(webView: com.tencent.smtt.sdk.WebView?) {
        this.mx5WebView = webView
    }

    override fun addJavaObjects(maps: MutableMap<String, Any>): JsInterfaceHolder {
        val sets = maps.entries
        for ((key, v) in sets) {
            val t = checkObject(v)
            if (!t) {
                throw JsInterfaceObjectException("This object has not offer method javascript to call ,please check addJavascriptInterface annotation was be added")
            } else {
                addJavaObjectDirect(key, v)
            }
        }
        return this
    }

    override fun addJavaObject(k: String, v: Any): JsInterfaceHolder {
        val t = checkObject(v)
        if (!t) {
            throw JsInterfaceObjectException("this object has not offer method javascript to call , please check addJavascriptInterface annotation was be added")
        } else {
            addJavaObjectDirect(k, v)
        }
        return this
    }

    @SuppressLint("JavascriptInterface")
    private fun addJavaObjectDirect(k: String, v: Any): JsInterfaceHolder {
        Timber.i("k:$k  v:$v")
        if (WebConfig.hasX5()) {
            this.mx5WebView?.addJavascriptInterface(v, k)
        } else {
            this.mWebView?.addJavascriptInterface(v, k)
        }
        return this
    }

    companion object {

        @JvmStatic
        fun getJsInterfaceHolder(webView: android.webkit.WebView?): JsInterfaceHolderImpl {
            return JsInterfaceHolderImpl(webView)
        }

        @JvmStatic
        fun getJsInterfaceHolder(webView: com.tencent.smtt.sdk.WebView?): JsInterfaceHolderImpl {
            return JsInterfaceHolderImpl(webView)
        }
    }

}
