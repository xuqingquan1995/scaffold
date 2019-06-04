package top.xuqingquan.web.agent

import android.widget.FrameLayout

interface LayoutParamsOffer<out T : FrameLayout.LayoutParams> {

    fun offerLayoutParams(): T

}
