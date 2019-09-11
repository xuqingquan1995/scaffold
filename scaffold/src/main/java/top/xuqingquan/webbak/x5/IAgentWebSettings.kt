package top.xuqingquan.webbak.x5

import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView

interface IAgentWebSettings<T : WebSettings> {

    fun getWebSettings(): T?

    fun toSetting(webView: WebView?): IAgentWebSettings<*>

}
