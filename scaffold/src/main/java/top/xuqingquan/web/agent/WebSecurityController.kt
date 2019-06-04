package top.xuqingquan.web.agent

interface WebSecurityController<T> {
    fun check(t: T)
}
