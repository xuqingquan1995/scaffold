package top.xuqingquan.web.agent

interface IWebIndicator<T : BaseIndicatorSpec> {

    fun offer(): T
}
