package top.xuqingquan.agentWeb

import android.app.Activity
import android.graphics.Bitmap
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.PopupMenu
import com.just.agentweb.*
import com.just.agentweb.download.DefaultDownloadImpl
import com.just.agentweb.download.DownloadListener
import com.just.agentweb.download.Extra
import dagger.Module
import dagger.Provides
import top.xuqingquan.R
import top.xuqingquan.di.scope.FragmentScope
import top.xuqingquan.utils.DeviceUtils
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019-04-29 22:45
 */
@Module
class AgentWebModule {

    @FragmentScope
    @Provides
    internal fun provideContext(fragment: AgentWebFragment) = fragment.mContext


    @FragmentScope
    @Provides
    internal fun provideAgentWeb(
        fragment: AgentWebFragment,
        settings: AbsAgentWebSettings,
        mWebViewClient: WebViewClient,
        mWebChromeClient: WebChromeClient,
        mPermissionInterceptor: PermissionInterceptor,
        mAgentWebUIControllerImplBase: AgentWebUIControllerImplBase,
        mMiddlewareWebChromeBase: MiddlewareWebChromeBase,
        mMiddlewareWebClientBase: MiddlewareWebClientBase
    ): AgentWeb {
        Timber.d("url=${fragment.url}")
        val mAgentWeb = AgentWeb.with(fragment)
            .setAgentWebParent(
                (fragment.view as LinearLayout),
                -1,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )//传入AgentWeb的父控件。
            .useDefaultIndicator(-1, 2)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
            .setAgentWebWebSettings(settings)//设置 IAgentWebSettings。
            .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
            .setWebChromeClient(mWebChromeClient) //WebChromeClient
            .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
            .setAgentWebUIController(mAgentWebUIControllerImplBase) //自定义UI  AgentWeb3.0.0 加入。
            .setMainFrameErrorView(
                R.layout.agentweb_error_page,
                -1
            ) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
            .useMiddlewareWebChrome(mMiddlewareWebChromeBase) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
            .useMiddlewareWebClient(mMiddlewareWebClientBase) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
            .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
            .createAgentWeb()//创建AgentWeb。
            .ready()//设置 WebSettings。
            .go(fragment.url)
        mAgentWeb.webCreator.webView.overScrollMode = WebView.OVER_SCROLL_NEVER
        return mAgentWeb
    }

    @FragmentScope
    @Provides
    internal fun provideMiddlewareWebClientBase(): MiddlewareWebClientBase {
        return object : MiddlewareWebClientBase() {}
    }

    @FragmentScope
    @Provides
    internal fun provideMiddlewareWebChromeBase(): MiddlewareWebChromeBase {
        return object : MiddlewareWebChromeBase() {}
    }

    @FragmentScope
    @Provides
    internal fun provideUIController(): AgentWebUIControllerImplBase {
        return AgentWebUIControllerImplBase()
    }

    @FragmentScope
    @Provides
    internal fun providePermissionInterceptor(): PermissionInterceptor {
        return PermissionInterceptor { _, _, _ ->
            /**
             * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
             * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
             */
            false
        }
    }

    @FragmentScope
    @Provides
    internal fun provideWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {}
    }

    @FragmentScope
    @Provides
    internal fun provideWebViewClient(fragment: AgentWebFragment): WebViewClient {
        return object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                fragment.pageNavigator(view?.canGoBack() == true)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (fragment.title.isNullOrEmpty()) {
                    fragment.tvTitle.text = view?.title
                }
            }
        }
    }

    @FragmentScope
    @Provides
    internal fun provideAbsAgentWebSettings(mSimpleDownloadListener: DownloadListener): AbsAgentWebSettings {
        return object : AbsAgentWebSettings() {
            lateinit var agentWeb: AgentWeb

            override fun bindAgentWebSupport(aw: AgentWeb) {
                agentWeb = aw
            }

            override fun setDownloader(
                webView: WebView?,
                downloadListener: android.webkit.DownloadListener?
            ): WebListenerManager {
                return super.setDownloader(
                    webView,
                    DefaultDownloadImpl
                        .create(
                            webView!!.context as Activity,
                            webView,
                            mSimpleDownloadListener,
                            agentWeb.permissionInterceptor
                        )
                )
            }
        }
    }

    @FragmentScope
    @Provides
    internal fun provideDownloadListener(): DownloadListener {
        return object : DownloadListener() {
            override fun onStart(
                url: String?,
                userAgent: String?,
                contentDisposition: String?,
                mimetype: String?,
                contentLength: Long,
                extra: Extra?
            ): Boolean {
                extra?.setBreakPointDownload(true) // 是否开启断点续传
                    ?.setConnectTimeOut(6000) // 连接最大时长
                    ?.setBlockMaxTime(10 * 60 * 1000)  // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                    ?.setDownloadTimeOut(java.lang.Long.MAX_VALUE) // 下载最大时长
                    ?.setParallelDownload(false)  // 串行下载更节省资源哦
                    ?.setEnableIndicator(true)  // false 关闭进度通知
//                    ?.addHeader("Cookie", "xx") // 自定义请求头
                    ?.setAutoOpen(true) // 下载完成自动打开
                    ?.setForceDownload(true) // 强制下载，不管网络网络类型
                return false
            }
        }
    }

    @FragmentScope
    @Provides
    internal fun providePopupMenu(fragment: AgentWebFragment): PopupMenu {
        val mPopupMenu = PopupMenu(fragment.requireContext(), fragment.menu)
        mPopupMenu.inflate(R.menu.agentweb)
        mPopupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.refresh -> fragment.mAgentWeb.urlLoader.reload()
                R.id.copy -> DeviceUtils.copyTextToBoard(
                    fragment.requireContext(),
                    fragment.mAgentWeb.webCreator.webView.url
                )
                R.id.share -> DeviceUtils.showSystemShareOption(
                    fragment.requireContext(),
                    fragment.mAgentWeb.webCreator.webView.title,
                    fragment.mAgentWeb.webCreator.webView.url
                )
            }
            return@setOnMenuItemClickListener false
        }
        return mPopupMenu
    }

}