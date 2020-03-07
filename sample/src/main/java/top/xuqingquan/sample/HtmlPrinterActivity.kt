package top.xuqingquan.sample

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.html_printer.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onEditorAction
import org.jetbrains.anko.toast
import top.xuqingquan.base.view.activity.SimpleActivity
import top.xuqingquan.utils.Timber
import top.xuqingquan.utils.copyTextToBoard
import top.xuqingquan.web.AgentWeb
import top.xuqingquan.web.nokernel.OpenOtherPageWays
import top.xuqingquan.web.nokernel.PermissionInterceptor
import top.xuqingquan.web.nokernel.WebConfig
import top.xuqingquan.web.x5.AbsAgentWebSettings

/**
 * Created by 许清泉 on 2019-10-28 14:50
 */
class HtmlPrinterActivity : SimpleActivity() {

    private var currentSource = ""
    private val html = MutableLiveData<String>()

    override fun getLayoutId() = R.layout.html_printer

    override fun initData(savedInstanceState: Bundle?) {
        Timber.d("WebConfig.hasX5()===>${WebConfig.hasX5()}")
        val agentWeb = AgentWeb.with(this)
            .setAgentWebParent(webView, -1, ViewGroup.LayoutParams(-1, -1))
            .useDefaultIndicator(-1, 0)
            .setAgentWebWebSettings(object : AbsAgentWebSettings() {
                override fun bindAgentWebSupport(agentWeb: AgentWeb) {
                }
            })
            .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
            .setWebViewClient(object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Timber.d("onPageFinished")
                    view?.loadUrl("javascript:window.android.getSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
                }
            })
            .setWebViewClient(object : android.webkit.WebViewClient() {
                override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Timber.d("onPageFinished")
                    view?.loadUrl("javascript:window.android.getSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
                }
            })
            .setPermissionInterceptor(object : PermissionInterceptor {
                override fun intercept(url: String?, permissions: Array<String>, action: String) =
                    false
            })
            .setOpenOtherPageWays(OpenOtherPageWays.DISALLOW)
            .createAgentWeb()//创建AgentWeb。
            .get()
//        webView.loadUrl("http://debugtbs.qq.com")
        agentWeb.jsInterfaceHolder.addJavaObject("android", JavaScriptObject(html))
        html.observe(this, Observer {
            toast("得到源码")
            currentSource = it?:""
        })
        url.onEditorAction { _, _, _ ->
            webView.loadUrl(url.text.toString().trim())
        }
        copy.onClick {
            copyTextToBoard(this@HtmlPrinterActivity, currentSource)
        }
        url.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.endsWith("\n")) {
                    url.setText(s.trim())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (webView.onKeyDown(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    class JavaScriptObject(private val html: MutableLiveData<String>) {

        @JavascriptInterface
        fun getSource(source: String) {
            html.postValue(source)
        }

    }
}