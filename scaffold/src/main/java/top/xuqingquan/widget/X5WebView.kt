package top.xuqingquan.widget

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.KeyEvent
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.just.agentwebX5.*
import org.jetbrains.anko.px2dip
import top.xuqingquan.BuildConfig
import top.xuqingquan.R
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019-05-22 21:00
 * 调用 [X5WebView.go]方法之前可以设置各种自定义的参数
 */
class X5WebView : FrameLayout {

    var agentWeb: AgentWeb? = null
        private set
    //错误页面id
    @ColorInt
    var indicatorColor: Int = -1//进度条颜色，-1为默认值
    @DimenRes
    var indicatorHeight: Int = 0 //进度条高度，高度为2，单位为dp
    var url: String? = "https://m.baidu.com"
    var debug: Boolean = false
        set(value) {
            field = value
            if (BuildConfig.DEBUG && field) {
                AgentWebConfig.debug()
            }
        }
        get() = BuildConfig.DEBUG && field

    var downloadListener = object : DownloadListener() {
        override fun onStart(
            url: String?,
            userAgent: String?,
            contentDisposition: String?,
            mimetype: String?,
            contentLength: Long,
            extra: Extra?
        ): Boolean {
            if (debug) {
                Timber.d("onStart-url=$url")
                Timber.d("onStart-userAgent=$userAgent")
                Timber.d("onStart-contentDisposition=$contentDisposition")
                Timber.d("onStart-mimetype=$mimetype")
                Timber.d("onStart-contentLength=$contentLength")
            }
            extra?.setBreakPointDownload(true) // 是否开启断点续传
                ?.setConnectTimeOut(6000) // 连接最大时长
                ?.setBlockMaxTime(10 * 60 * 1000)  // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                ?.setDownloadTimeOut(java.lang.Long.MAX_VALUE) // 下载最大时长
                ?.setParallelDownload(false)  // 串行下载更节省资源哦
                ?.setEnableIndicator(true)  // false 关闭进度通知
                // ?.addHeader("Cookie", "xx") // 自定义请求头
                ?.setAutoOpen(true) // 下载完成自动打开
                ?.setForceDownload(true) // 强制下载，不管网络网络类型
            return false
        }

        override fun onProgress(url: String?, downloaded: Long, length: Long, usedTime: Long) {
            super.onProgress(url, downloaded, length, usedTime)
            if (debug) {
                Timber.d("onProgress-url=$url")
                Timber.d("onProgress-downloaded=$downloaded")
                Timber.d("onProgress-length=$length")
                Timber.d("onProgress-usedTime=$usedTime")
            }
        }

        override fun onResult(throwable: Throwable?, path: Uri?, url: String?, extra: Extra?): Boolean {
            if (debug) {
                Timber.d("onResult-throwable=$throwable")
                Timber.d("onResult-path=$path")
                Timber.d("onResult-url=$url")
            }
            return super.onResult(throwable, path, url, extra)
        }
    }

    //设置 IAgentWebSettings。
    var absAgentWebSettings = object : AbsAgentWebSettings() {
        var agentWeb: AgentWeb? = null
        override fun bindAgentWebSupport(aw: AgentWeb?) {
            agentWeb = aw
        }

        override fun setDownloader(
            webView: com.tencent.smtt.sdk.WebView?,
            downloadListener: com.tencent.smtt.sdk.DownloadListener?
        ): WebListenerManager {
            return super.setDownloader(
                webView,
                DefaultDownloadImpl
                    .create(
                        context as Activity,
                        webView!!,
                        this@X5WebView.downloadListener,
                        agentWeb?.permissionInterceptor
                    )
            )
        }
    }


    var webViewClient = object : WebViewClient() {}

    var webChromeClient = object : WebChromeClient() {}

    var permissionInterceptor = PermissionInterceptor { _, _, _ ->
        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        false
    }

    var agentWebUIControllerImplBase = AgentWebUIControllerImplBase()

    var middlewareWebChromeBase = object : MiddlewareWebChromeBase() {}

    var middlewareWebClientBase = object : MiddlewareWebClientBase() {}


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

    private fun initAgentWeb() {
        agentWeb = AgentWeb.with(context as Activity)
            .setAgentWebParent(
                this,
                -1,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )//传入AgentWeb的父控件。
            .useDefaultIndicator(indicatorColor, indicatorHeight)
            .setAgentWebWebSettings(absAgentWebSettings)//设置 IAgentWebSettings。
            .setWebViewClient(webViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
            .setWebChromeClient(webChromeClient) //WebChromeClient
            .setPermissionInterceptor(permissionInterceptor) //权限拦截 2.0.0 加入。
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
            .setAgentWebUIController(agentWebUIControllerImplBase) //自定义UI  AgentWeb3.0.0 加入。
            .useMiddlewareWebChrome(middlewareWebChromeBase) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
            .useMiddlewareWebClient(middlewareWebClientBase) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
            .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
            .createAgentWeb()//创建AgentWeb。
            .ready()//设置 IAgentWebSettings。
            .get()
        agentWeb!!.webCreator.webView.overScrollMode = WebView.OVER_SCROLL_NEVER
        agentWeb!!.agentWebSettings.webSettings.loadWithOverviewMode = true
        agentWeb!!.agentWebSettings.webSettings.useWideViewPort = true
    }

    /**
     * 在调用该方法前可以自定义其他参数
     */
    fun go(url: String = this@X5WebView.url!!) {
        if (agentWeb == null) {
            initAgentWeb()
        }
        agentWeb!!.urlLoader.loadUrl(url)
    }

    fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
    }

    fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
    }

    fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (agentWeb?.handleKeyEvent(keyCode, event) == true) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun reload() = agentWeb?.urlLoader?.reload()

    fun getCurrentUrl() = agentWeb?.webCreator?.webView?.url

}
