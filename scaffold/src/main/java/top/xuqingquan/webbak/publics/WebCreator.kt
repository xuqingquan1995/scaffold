package top.xuqingquan.webbak.publics

import android.widget.FrameLayout
import top.xuqingquan.webbak.nokernel.BaseIndicatorSpec
import top.xuqingquan.webbak.nokernel.IWebIndicator

interface WebCreator<T : BaseIndicatorSpec> : IWebIndicator<T> {

    fun getWebView(): android.webkit.WebView?

    fun getX5WebView(): com.tencent.smtt.sdk.WebView?

    fun getWebParentLayout(): FrameLayout

    fun create(): WebCreator<T>
}
