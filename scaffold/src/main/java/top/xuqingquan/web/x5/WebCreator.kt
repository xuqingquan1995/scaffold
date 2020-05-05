package top.xuqingquan.web.x5

import android.widget.FrameLayout
import top.xuqingquan.web.nokernel.BaseIndicatorSpec
import top.xuqingquan.web.nokernel.IWebIndicator

interface WebCreator<T : BaseIndicatorSpec> : IWebIndicator<T> {

    fun getWebView(): com.tencent.smtt.sdk.WebView?

    fun getWebParentLayout(): FrameLayout

    fun create(): WebCreator<T>

    fun getWebViewType(): Int
}
