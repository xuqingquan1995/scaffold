package top.xuqingquan.web.publics

import android.view.View

interface IVideo {


    val isVideoState: Boolean

    fun onShowCustomView(view: View, callback: android.webkit.WebChromeClient.CustomViewCallback)

    fun onShowCustomView(
        view: View,
        callback: com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback
    )

    fun onHideCustomView()

}
