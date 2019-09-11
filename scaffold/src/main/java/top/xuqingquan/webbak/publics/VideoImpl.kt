package top.xuqingquan.webbak.publics

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.util.Pair
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import top.xuqingquan.webbak.nokernel.EventInterceptor
import top.xuqingquan.webbak.nokernel.WebConfig

import java.util.HashSet

class VideoImpl : IVideo, EventInterceptor {

    private var mActivity: Activity? = null
    private var mWebView: android.webkit.WebView? = null
    private var mx5WebView: com.tencent.smtt.sdk.WebView? = null
    private var mFlags: MutableSet<Pair<Int, Int>>? = null
    private var mMoiveView: View? = null
    private var mMoiveParentView: ViewGroup? = null
    private var mCallback: android.webkit.WebChromeClient.CustomViewCallback? = null
    private var mx5Callback: IX5WebChromeClient.CustomViewCallback? = null

    override val isVideoState: Boolean
        get() = mMoiveView != null

    constructor(mActivity: Activity, webView: android.webkit.WebView?) {
        this.mActivity = mActivity
        this.mWebView = webView
        mFlags = HashSet()
    }

    constructor(mActivity: Activity, webView: com.tencent.smtt.sdk.WebView?) {
        this.mActivity = mActivity
        this.mx5WebView = webView
        mFlags = HashSet()
    }

    override fun onShowCustomView(view: View, callback: android.webkit.WebChromeClient.CustomViewCallback) {
        val mActivity = this.mActivity
        if (mActivity == null || mActivity.isFinishing) {
            return
        }
        mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val mWindow = mActivity.window
        var mPair: Pair<Int, Int>
        // 保存当前屏幕的状态
        if (mWindow.attributes.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON == 0) {
            mPair = Pair(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 0)
            mWindow.setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
            mFlags!!.add(mPair)
        }
        if (mWindow.attributes.flags and WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED == 0) {
            mPair = Pair(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, 0)
            mWindow.setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            )
            mFlags!!.add(mPair)
        }
        if (mMoiveView != null) {
            callback.onCustomViewHidden()
            return
        }
        mWebView?.visibility = View.GONE
        if (mMoiveParentView == null) {
            val mDecorView = mActivity.window.decorView as FrameLayout
            mMoiveParentView = FrameLayout(mActivity)
            mMoiveParentView!!.setBackgroundColor(Color.BLACK)
            mDecorView.addView(mMoiveParentView)
        }
        this.mCallback = callback
        this.mMoiveView = view
        mMoiveParentView!!.addView(view)
        mMoiveParentView!!.visibility = View.VISIBLE
    }

    override fun onShowCustomView(view: View, callback: IX5WebChromeClient.CustomViewCallback) {
        val mActivity = this.mActivity
        if (mActivity == null || mActivity.isFinishing) {
            return
        }
        mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val mWindow = mActivity.window
        var mPair: Pair<Int, Int>
        // 保存当前屏幕的状态
        if (mWindow.attributes.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON == 0) {
            mPair = Pair(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 0)
            mWindow.setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
            mFlags!!.add(mPair)
        }
        if (mWindow.attributes.flags and WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED == 0) {
            mPair = Pair(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, 0)
            mWindow.setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            )
            mFlags!!.add(mPair)
        }
        if (mMoiveView != null) {
            callback.onCustomViewHidden()
            return
        }
        mx5WebView?.visibility = View.GONE
        if (mMoiveParentView == null) {
            val mDecorView = mActivity.window.decorView as FrameLayout
            mMoiveParentView = FrameLayout(mActivity)
            mMoiveParentView!!.setBackgroundColor(Color.BLACK)
            mDecorView.addView(mMoiveParentView)
        }
        this.mx5Callback = callback
        this.mMoiveView = view
        mMoiveParentView!!.addView(view)
        mMoiveParentView!!.visibility = View.VISIBLE
    }

    override fun onHideCustomView() {
        if (mMoiveView == null) {
            return
        }
        if (mActivity != null && mActivity!!.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mActivity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        if (mFlags!!.isNotEmpty()) {
            for (mPair in mFlags!!) {
                if (mPair.first != null && mPair.second != null) {
                    mActivity?.window?.setFlags(mPair.second!!, mPair.first!!)
                }
            }
            mFlags!!.clear()
        }
        mMoiveView!!.visibility = View.GONE
        if (mMoiveParentView != null && mMoiveView != null) {
            mMoiveParentView!!.removeView(mMoiveView)

        }
        mMoiveParentView?.visibility = View.GONE
        this.mMoiveView = null
        if (WebConfig.hasX5()) {
            mx5Callback?.onCustomViewHidden()
            mx5WebView?.visibility = View.VISIBLE
        } else {
            mCallback?.onCustomViewHidden()
            mWebView?.visibility = View.VISIBLE
        }
    }

    override fun event(): Boolean {
        return if (isVideoState) {
            onHideCustomView()
            true
        } else {
            false
        }
    }
}
