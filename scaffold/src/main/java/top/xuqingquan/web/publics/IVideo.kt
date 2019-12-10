package top.xuqingquan.web.publics

import android.view.View

interface IVideo {

    fun onShowCustomView(view: View, callback: android.webkit.WebChromeClient.CustomViewCallback)

    fun onHideCustomView()

    fun isVideoState(): Boolean

}
