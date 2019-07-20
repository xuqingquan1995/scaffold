package top.xuqingquan.web.publics

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import top.xuqingquan.R
import top.xuqingquan.utils.Timber
import top.xuqingquan.web.nokernel.Provider

class WebParentLayout @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = -1
) : FrameLayout(context, attrs, defStyleAttr), Provider<AbsAgentWebUIController> {
    private var mAgentWebUIController: AbsAgentWebUIController? = null
    @LayoutRes
    private var mErrorLayoutRes: Int = 0
    @IdRes
    private var mClickId = -1
    private var mErrorView: View? = null
    var webView: android.webkit.WebView? = null
        private set
    var x5WebView: com.tencent.smtt.sdk.WebView? = null
        private set
    private var mErrorLayout: FrameLayout? = null

    constructor(context: Context) : this(context, null) {
        Timber.i("WebParentLayout")
    }

    init {
        if (context !is Activity) {
            throw IllegalArgumentException("WebParentLayout context must be activity or activity sub class .")
        }
        this.mErrorLayoutRes = R.layout.agentweb_error_page
    }

    fun bindController(agentWebUIController: AbsAgentWebUIController) {
        this.mAgentWebUIController = agentWebUIController
        this.mAgentWebUIController!!.bindWebParent(this, context as Activity)
    }

    fun showPageMainFrameError() {
        var container: View? = this.mErrorLayout
        if (container != null) {
            container.visibility = View.VISIBLE
        } else {
            createErrorLayout()
            container = this.mErrorLayout
        }
        val clickView = container!!.findViewById<View>(mClickId)
        if (mClickId != -1 && clickView != null) {
            clickView.isClickable = true
        } else {
            container.isClickable = true
        }
    }

    private fun createErrorLayout() {
        val mFrameLayout = FrameLayout(context)
        mFrameLayout.setBackgroundColor(Color.WHITE)
        mFrameLayout.id = R.id.mainframe_error_container_id
        if (this.mErrorView == null) {
            val mLayoutInflater = LayoutInflater.from(context)
            Timber.i("mErrorLayoutRes:$mErrorLayoutRes")
            mLayoutInflater.inflate(mErrorLayoutRes, mFrameLayout, true)
        } else {
            mFrameLayout.addView(mErrorView)
        }
        val mViewStub = this.findViewById<ViewStub>(R.id.mainframe_error_viewsub_id)
        val index = this.indexOfChild(mViewStub)
        this.removeViewInLayout(mViewStub)
        val layoutParams = layoutParams
        this.mErrorLayout = mFrameLayout
        if (layoutParams != null) {
            this.addView(mFrameLayout, index, layoutParams)
        } else {
            this.addView(mFrameLayout, index)
        }
        mFrameLayout.visibility = View.VISIBLE
        if (mClickId != -1) {
            val clickView = mFrameLayout.findViewById<View>(mClickId)
            if (clickView != null) {
                clickView.setOnClickListener { v ->
                    if (webView != null) {
                        clickView.isClickable = false
                        webView!!.reload()
                    }
                }
                return
            } else {
                Timber.e("ClickView is null , cannot bind accurate view to refresh or reload .")
            }
        }
        mFrameLayout.setOnClickListener {
            if (webView != null) {
                mFrameLayout.isClickable = false
                webView!!.reload()
            }
        }
    }

    fun hideErrorLayout() {
        val mView = this.findViewById<View>(R.id.mainframe_error_container_id)
        mView?.visibility = View.GONE
    }

    fun setErrorView(errorView: View?) {
        this.mErrorView = errorView
    }

    fun setErrorLayoutRes(@LayoutRes resLayout: Int, @IdRes id: Int) {
        this.mClickId = id
        if (this.mClickId <= 0) {
            this.mClickId = -1
        }
        this.mErrorLayoutRes = resLayout
        if (this.mErrorLayoutRes <= 0) {
            this.mErrorLayoutRes = R.layout.agentweb_error_page
        }
    }

    override fun provide(): AbsAgentWebUIController? {
        return this.mAgentWebUIController
    }

    fun bindWebView(view: android.webkit.WebView) {
        if (this.webView == null) {
            this.webView = view
        }
    }

    fun bindWebView(view: com.tencent.smtt.sdk.WebView) {
        if (this.x5WebView == null) {
            this.x5WebView = view
        }
    }

}
