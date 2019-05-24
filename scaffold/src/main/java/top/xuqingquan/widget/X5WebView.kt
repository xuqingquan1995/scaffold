package top.xuqingquan.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import com.just.agentwebX5.*
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebViewClient
import org.jetbrains.anko.px2dip
import top.xuqingquan.BuildConfig
import top.xuqingquan.R
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019-05-24 18:18
 */
class X5WebView : FrameLayout {

    var agentWebX5: AgentWebX5? = null
        private set
    var url: String? = "https://m.baidu.com"
    @ColorInt
    var indicatorColor: Int = -1//进度条颜色，-1为默认值
    @DimenRes
    var indicatorHeight: Int = 0 //进度条高度，高度为2，单位为dp
    var debug: Boolean = false
        set(value) {
            field = value
            if (BuildConfig.DEBUG && field) {
                AgentWebX5Config.DEBUG = true
            }
        }
        get() = AgentWebX5Config.DEBUG

    var webSettings = object : WebDefaultSettingsManager() {}

    var webViewClient = object : WebViewClient() {}

    var webChromeClient = object : WebChromeClient() {}

    var titleCallback = ChromeClientCallbackManager.ReceivedTitleCallback { view, title ->
        if (debug) {
            Timber.d("titleCallback-title=$title")
        }
    }

    var permissionInterceptor = PermissionInterceptor { _, _, _ ->
        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        false
    }

    var middleWareWebChrome: MiddleWareWebChromeBase = object : MiddleWareWebChromeBase() {}

    var middleWareWebClient: MiddleWareWebClientBase = object : MiddleWareWebClientBase() {}

    var downLoadResultListener = object : DownLoadResultListener {
        override fun error(path: String?, resUrl: String?, cause: String?, e: Throwable?) {
            if (debug) {
                Timber.d("error-path=$path")
                Timber.d("error-resUrl=$resUrl")
                Timber.d("error-cause=$cause")
                Timber.d("error-e?.message=${e?.message}")
            }
        }

        override fun success(path: String?) {
            if (debug) {
                Timber.d("success-path=$path")
            }
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.X5WebView)
        typedArray.getString(R.styleable.X5WebView_x5_url)?.let {
            url = it
        }
        debug = typedArray.getBoolean(R.styleable.X5WebView_x5_debug, false)
        indicatorColor = typedArray.getColor(R.styleable.X5WebView_x5_indicatorColor, -1)
        indicatorHeight = typedArray.getDimension(R.styleable.X5WebView_x5_indicatorHeight, -1f).toInt()
        indicatorHeight = if (indicatorHeight == -1) {
            2
        } else {
            px2dip(indicatorHeight).toInt()
        }
        typedArray.recycle()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun initAgentWebX5() {
        agentWebX5 = AgentWebX5.with(context as Activity)
            .setAgentWebParent(
                this,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )//传入AgentWeb的父控件。
            .useDefaultIndicator()
            .setIndicatorColorWithHeight(indicatorColor, indicatorHeight)
            .setWebSettings(webSettings)
            .setWebViewClient(webViewClient)
            .setWebChromeClient(webChromeClient)
            .setReceivedTitleCallback(titleCallback)
            .setPermissionInterceptor(permissionInterceptor)
            .useMiddleWareWebChrome(middleWareWebChrome)
            .useMiddleWareWebClient(middleWareWebClient)
            .setNotifyIcon(R.drawable.ic_download)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
            .interceptUnkownScheme()
            .openParallelDownload()
            .setSecutityType(AgentWebX5.SecurityType.strict)
            .addDownLoadResultListener(downLoadResultListener)
            .createAgentWeb()
            .ready()
            .go(url)
    }

    fun go(url: String = this@X5WebView.url!!) {
        if (agentWebX5 == null) {
            initAgentWebX5()
        }
        agentWebX5!!.loader.loadUrl(url)
    }

    fun onResume() {
        agentWebX5?.webLifeCycle?.onResume()
    }


    fun onPause() {
        agentWebX5?.webLifeCycle?.onPause()
    }

    fun onDestroy() {
        agentWebX5?.webLifeCycle?.onDestroy()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (agentWebX5?.handleKeyEvent(keyCode, event) == true) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun reload() = agentWebX5?.loader?.reload()

    fun getCurrentUrl() = agentWebX5?.webCreator?.get()?.url

}
