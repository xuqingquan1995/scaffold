package top.xuqingquan.web.agent

interface JsInterfaceHolder {

    fun addJavaObjects(maps: Map<String, Any>): JsInterfaceHolder

    fun addJavaObject(k: String, v: Any): JsInterfaceHolder

    fun checkObject(v: Any): Boolean

}
