package top.xuqingquan.web.publics

import top.xuqingquan.web.nokernel.BaseIndicatorSpec

class IndicatorHandler : IndicatorController {
    private var mBaseIndicatorSpec: BaseIndicatorSpec? = null

    override fun progress(v: android.webkit.WebView, newProgress: Int) {
        progress(newProgress)
    }

    override fun progress(v: com.tencent.smtt.sdk.WebView, newProgress: Int) {
        progress(newProgress)
    }

    private fun progress(newProgress: Int) {
        when (newProgress) {
            0 -> reset()
            in 1..10 -> showIndicator()
            in 11..94 -> setProgress(newProgress)
            else -> {
                setProgress(newProgress)
                finish()
            }
        }
    }

    override fun offerIndicator(): BaseIndicatorSpec {
        return this.mBaseIndicatorSpec!!
    }

    fun reset() {
        mBaseIndicatorSpec?.reset()
    }

    override fun finish() {
        mBaseIndicatorSpec?.hide()
    }

    override fun setProgress(n: Int) {
        mBaseIndicatorSpec?.setProgress(n)
    }

    override fun showIndicator() {
        mBaseIndicatorSpec?.show()
    }


    fun inJectIndicator(baseIndicatorSpec: BaseIndicatorSpec): IndicatorHandler {
        this.mBaseIndicatorSpec = baseIndicatorSpec
        return this
    }

    companion object {

        @JvmStatic
        val instance: IndicatorHandler
            get() = IndicatorHandler()
    }
}
