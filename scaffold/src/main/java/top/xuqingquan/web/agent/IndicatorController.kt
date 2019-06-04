package top.xuqingquan.web.agent

/**
 * Created by 许清泉 on 2019-06-05 00:53
 */
interface IndicatorController {

    fun offerIndicator(): BaseIndicatorSpec

    fun showIndicator()

    fun setProgress(newProgress: Int)

    fun finish()
}