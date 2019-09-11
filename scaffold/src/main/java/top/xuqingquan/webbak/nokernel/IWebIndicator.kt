package top.xuqingquan.webbak.nokernel

interface IWebIndicator<T : BaseIndicatorSpec> {

    fun offer(): T
}
