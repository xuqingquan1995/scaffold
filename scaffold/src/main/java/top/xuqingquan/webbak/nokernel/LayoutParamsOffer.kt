package top.xuqingquan.webbak.nokernel

import android.widget.FrameLayout

interface LayoutParamsOffer<T : FrameLayout.LayoutParams> {

    fun offerLayoutParams(): T

}
