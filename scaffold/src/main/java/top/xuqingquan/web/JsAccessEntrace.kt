package top.xuqingquan.web

import com.tencent.smtt.sdk.ValueCallback
import top.xuqingquan.web.x5.QuickCallJs

interface JsAccessEntrace : QuickCallJs {

    fun callJs(js: String, callback: ValueCallback<String>)

    fun callJs(js: String)

}
