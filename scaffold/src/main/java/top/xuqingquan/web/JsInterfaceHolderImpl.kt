package top.xuqingquan.web

import com.tencent.smtt.sdk.WebView
import top.xuqingquan.utils.Timber
import top.xuqingquan.web.agent.JsBaseInterfaceHolder
import top.xuqingquan.web.agent.JsInterfaceHolder
import top.xuqingquan.web.agent.JsInterfaceObjectException

class JsInterfaceHolderImpl private constructor(private val mWebView: WebView) : JsBaseInterfaceHolder() {

    override fun addJavaObjects(maps: Map<String, Any>): JsInterfaceHolder {
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
        return this
    }

    private fun addJavaObjectDirect(k: String, v: Any): JsInterfaceHolder {
        Timber.i("k:$k  v:$v")
        this.mWebView.addJavascriptInterface(v, k)
        return this
    }

    companion object {

        @JvmStatic
        fun getJsInterfaceHolder(webView: WebView): JsInterfaceHolderImpl {
            return JsInterfaceHolderImpl(webView)
        }
    }

}
