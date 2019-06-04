package top.xuqingquan.web.agent

import android.webkit.JavascriptInterface
import top.xuqingquan.web.x5.X5WebConfig

abstract class JsBaseInterfaceHolder : JsInterfaceHolder {

    override fun checkObject(v: Any): Boolean {
        if (AgentWebConfig.WEBVIEW_TYPE == AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE) {
            return true
        }
        var tag = false
        val clazz = v.javaClass
        val mMethods = clazz.methods
        for (mMethod in mMethods) {
            val mAnnotations = mMethod.annotations
            for (mAnnotation in mAnnotations) {
                if (mAnnotation is JavascriptInterface) {
                    tag = true
                    break
                }
            }
            if (tag) {
                break
            }
        }
        return tag
    }

}
