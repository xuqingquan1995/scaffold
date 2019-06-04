package top.xuqingquan.web.x5

import com.tencent.smtt.sdk.ValueCallback
import top.xuqingquan.web.agent.QuickCallJs

interface QuickCallJs :QuickCallJs{

    fun quickCallJs(method: String, callback: ValueCallback<String>, vararg params: String)

}
