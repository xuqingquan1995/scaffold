package top.xuqingquan.web.agent

interface PermissionInterceptor {

    fun intercept(url: String, permissions: Array<String>, action: String): Boolean

}
