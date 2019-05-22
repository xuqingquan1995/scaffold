package top.xuqingquan.agentWeb

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.FrameLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import top.xuqingquan.BuildConfig
import top.xuqingquan.R
import top.xuqingquan.app.AppComponentUtils
import javax.inject.Inject

/**
 * Created by 许清泉 on 2019-05-22 21:00
 */
class AgentWebView : FrameLayout {

    @Inject
    lateinit var mAgentWeb: AgentWeb
    //错误页面id
    var error_page = -1
        private set
    //错误页面的刷新控件id，-1为整个页面都可以刷新
    var refreshId = -1
        private set

    var url: String? = "https://m.baidu.com"
        set(value) {//set的时候重新加载网页
            mAgentWeb.urlLoader.loadUrl(value)
            field = value
        }
    var debug: Boolean = BuildConfig.DEBUG
        set(value) {
            if (BuildConfig.DEBUG && value) {
                AgentWebConfig.debug()
            }
            field = value
        }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AgentWebView)
        typedArray.getString(R.styleable.AgentWebView_url)?.let {
            url = it
        }
        debug = typedArray.getBoolean(R.styleable.AgentWebView_debug, BuildConfig.DEBUG)
        error_page = typedArray.getResourceId(R.styleable.AgentWebView_error_page, R.layout.agentweb_error_page)
        refreshId = typedArray.getResourceId(R.styleable.AgentWebView_refreshId, -1)
        typedArray.recycle()
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun initView(context: Context) {
        DaggerAgentWebComponent.builder().appComponent(AppComponentUtils.obtainAppComponentFromContext(context))
            .view(this).build().inject(this)
    }

    fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
    }

    fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
    }

    fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun reload() = mAgentWeb.urlLoader.reload()

    fun getCurrentUrl() = mAgentWeb.webCreator.webView.url

}
