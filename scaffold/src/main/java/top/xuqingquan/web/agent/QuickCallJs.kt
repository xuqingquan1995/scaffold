package top.xuqingquan.web.agent

/**
 * Created by 许清泉 on 2019-06-05 00:58
 */
interface QuickCallJs {

    fun quickCallJs(method: String, vararg params: String)

    fun quickCallJs(method: String)
}