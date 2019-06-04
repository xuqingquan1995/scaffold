package top.xuqingquan.web.agent

interface BaseIndicatorSpec {

    fun show()

    fun hide()

    fun reset()

    fun setProgress(newProgress: Int)

}
