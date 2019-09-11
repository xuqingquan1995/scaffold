package top.xuqingquan.webbak.publics

interface QuickCallJs {

    fun quickCallJs(method: String?, callback: android.webkit.ValueCallback<String>?, vararg params: String?)

    fun quickCallJs(method: String?, callback: com.tencent.smtt.sdk.ValueCallback<String>?, vararg params: String?)

    fun quickCallJs(method: String?, vararg params: String?)

    fun quickCallJs(method: String?)
}
