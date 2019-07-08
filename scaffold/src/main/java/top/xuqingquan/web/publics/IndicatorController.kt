package top.xuqingquan.web.publics

import top.xuqingquan.web.nokernel.BaseIndicatorSpec

interface IndicatorController {

    fun progress(v: android.webkit.WebView, newProgress: Int)

    fun progress(v: com.tencent.smtt.sdk.WebView, newProgress: Int)

    fun offerIndicator(): BaseIndicatorSpec?

    fun showIndicator()

    fun setProgress(newProgress: Int)

    fun finish()
}
