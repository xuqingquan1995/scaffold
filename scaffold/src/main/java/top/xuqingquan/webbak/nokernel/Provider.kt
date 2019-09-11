package top.xuqingquan.webbak.nokernel

interface Provider<T> {
    fun provide(): T?
}
