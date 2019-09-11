package top.xuqingquan.web.publics

import android.app.Activity
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import top.xuqingquan.utils.Timber
import top.xuqingquan.web.nokernel.WebConfig

class DefaultDesignUIController : DefaultUIController() {

    private var mBottomSheetDialog: BottomSheetDialog? = null
    private var mActivity: Activity? = null
    private var mWebParentLayout: WebParentLayout? = null
    private var mLayoutInflater: LayoutInflater? = null

    override fun onJsAlert(view: android.webkit.WebView, url: String, message: String) {
        onJsAlertInternal(view, message)
    }

    override fun onJsAlert(view: com.tencent.smtt.sdk.WebView, url: String, message: String) {
        onJsAlertInternal(view, message)
    }

    private fun onJsAlertInternal(view: android.webkit.WebView, message: String) {
        if (this.mActivity == null || this.mActivity!!.isFinishing||this.mActivity!!.isDestroyed) {
            return
        }
        try {
            AgentWebUtils.show(
                view,
                message,
                Snackbar.LENGTH_SHORT,
                Color.WHITE,
                ContextCompat.getColor(this.mActivity!!, android.R.color.black),
                null,
                -1, null
            )
        } catch (throwable: Throwable) {
            Timber.e(throwable)
        }

    }

    private fun onJsAlertInternal(view: com.tencent.smtt.sdk.WebView, message: String) {
        if (this.mActivity == null || this.mActivity!!.isFinishing||this.mActivity!!.isDestroyed) {
            return
        }
        try {
            AgentWebUtils.show(
                view,
                message,
                Snackbar.LENGTH_SHORT,
                Color.WHITE,
                ContextCompat.getColor(this.mActivity!!, android.R.color.black),
                null,
                -1, null
            )
        } catch (throwable: Throwable) {
            Timber.e(throwable)
        }

    }

    override fun onSelectItemsPrompt(
        view: android.webkit.WebView,
        url: String,
        ways: Array<String>,
        callback: Handler.Callback
    ) {
        showChooserInternal(url, ways, callback)
    }

    override fun onSelectItemsPrompt(
        view: com.tencent.smtt.sdk.WebView,
        url: String,
        ways: Array<String>,
        callback: Handler.Callback
    ) {
        showChooserInternal(url, ways, callback)
    }

    private fun showChooserInternal(url: String, ways: Array<String>, callback: Handler.Callback?) {
        if (this.mActivity == null || this.mActivity!!.isFinishing||this.mActivity!!.isDestroyed) {
            return
        }
        Timber.i("url:" + url + "  ways:" + ways[0])
        var mRecyclerView: RecyclerView?
        if (mBottomSheetDialog == null) {
            mBottomSheetDialog = BottomSheetDialog(mActivity!!)
            mRecyclerView = RecyclerView(mActivity!!)
            mRecyclerView.layoutManager = LinearLayoutManager(mActivity)
            mRecyclerView.id = RECYCLERVIEW_ID
            mBottomSheetDialog!!.setContentView(mRecyclerView)
        }
        mRecyclerView = mBottomSheetDialog!!.delegate.findViewById(RECYCLERVIEW_ID)
        mRecyclerView?.adapter = getAdapter(ways, callback)
        mBottomSheetDialog!!.setOnCancelListener {
            callback?.handleMessage(Message.obtain(null, -1))
        }
        mBottomSheetDialog!!.show()
    }

    private fun getAdapter(ways: Array<String>, callback: Handler.Callback?): RecyclerView.Adapter<*> {
        return object : RecyclerView.Adapter<BottomSheetHolder>() {
            override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BottomSheetHolder {
                return BottomSheetHolder(
                    mLayoutInflater!!.inflate(
                        android.R.layout.simple_list_item_1,
                        viewGroup,
                        false
                    )
                )
            }

            override fun onBindViewHolder(bottomSheetHolder: BottomSheetHolder, i: Int) {
                val outValue = TypedValue()
                mActivity!!.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                bottomSheetHolder.mTextView.setBackgroundResource(outValue.resourceId)
                bottomSheetHolder.mTextView.text = ways[i]
                bottomSheetHolder.mTextView.setOnClickListener {
                    if (mBottomSheetDialog != null && mBottomSheetDialog!!.isShowing) {
                        mBottomSheetDialog!!.dismiss()
                    }
                    val mMessage = Message.obtain()
                    mMessage.what = i
                    callback!!.handleMessage(mMessage)
                }
            }

            override fun getItemCount(): Int {
                return ways.size
            }
        }
    }

    private class BottomSheetHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var mTextView: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun bindSupportWebParent(webParentLayout: WebParentLayout, activity: Activity) {
        super.bindSupportWebParent(webParentLayout, activity)
        this.mActivity = activity
        this.mWebParentLayout = webParentLayout
        mLayoutInflater = LayoutInflater.from(mActivity)
    }

    override fun onShowMessage(message: String, intent: String) {
        if (this.mActivity == null || this.mActivity!!.isFinishing||this.mActivity!!.isDestroyed) {
            return
        }
        if (!TextUtils.isEmpty(intent) && intent.contains("performDownload")) {
            return
        }
        if (WebConfig.hasX5()) {
            onJsAlertInternal(mWebParentLayout!!.x5WebView!!, message)
        } else {
            onJsAlertInternal(mWebParentLayout!!.webView!!, message)
        }
    }

    companion object {
        private const val RECYCLERVIEW_ID = 0x1001
    }
}
