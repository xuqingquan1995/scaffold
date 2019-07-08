package top.xuqingquan.web.publics

interface JsAccessEntrace : QuickCallJs {

    fun callJs(js: String, callback: android.webkit.ValueCallback<String>)

    fun callJs(js: String, callback: com.tencent.smtt.sdk.ValueCallback<String>)

    fun callJs(js: String)

}
