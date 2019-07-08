@file:Suppress("DEPRECATION")

package top.xuqingquan.web.publics

import android.app.Activity
import android.app.ProgressDialog
import android.content.res.Resources
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import top.xuqingquan.R
import top.xuqingquan.utils.DeviceUtils
import top.xuqingquan.utils.Timber
import top.xuqingquan.web.nokernel.WebUtils

open class DefaultUIController : AbsAgentWebUIController() {

    private var mAlertDialog: AlertDialog? = null
    private var mConfirmDialog: AlertDialog? = null
    private var mJsPromptResult: android.webkit.JsPromptResult? = null
    private var mJsResult: android.webkit.JsResult? = null
    private var mX5JsPromptResult: com.tencent.smtt.export.external.interfaces.JsPromptResult? = null
    private var mX5JsResult: com.tencent.smtt.export.external.interfaces.JsResult? = null
    private var mPromptDialog: AlertDialog? = null
    private var mActivity: Activity? = null
    private var mWebParentLayout: WebParentLayout? = null
    private var mAskOpenOtherAppDialog: AlertDialog? = null
    private var mProgressDialog: ProgressDialog? = null
    private var mAlertDialogForceDownload: AlertDialog? = null
    private var mResources: Resources? = null

    override fun onJsAlert(view: android.webkit.WebView, url: String, message: String) {
        WebUtils.toastShowShort(view.context.applicationContext, message)
    }

    override fun onJsAlert(view: com.tencent.smtt.sdk.WebView, url: String, message: String) {
        WebUtils.toastShowShort(view.context.applicationContext, message)
    }

    override fun onOpenPagePrompt(view: android.webkit.WebView, url: String, callback: Handler.Callback) {
        onOpenPagePrompt(callback)
    }

    override fun onOpenPagePrompt(view: com.tencent.smtt.sdk.WebView, url: String, callback: Handler.Callback) {
        onOpenPagePrompt(callback)
    }

    private fun onOpenPagePrompt(callback: Handler.Callback?) {
        Timber.i("onOpenPagePrompt")
        if (mAskOpenOtherAppDialog == null) {
            mAskOpenOtherAppDialog = AlertDialog.Builder(mActivity!!)
                .setMessage(
                    mResources!!.getString(
                        R.string.agentweb_leave_app_and_go_other_page,
                        DeviceUtils.getApplicationName(mActivity!!)
                    )
                )//
                .setTitle(mResources!!.getString(R.string.agentweb_tips))
                .setNegativeButton(android.R.string.cancel) { dialog, which ->
                    callback?.handleMessage(Message.obtain(null, -1))
                }//
                .setPositiveButton(mResources!!.getString(R.string.agentweb_leave)) { dialog, which ->
                    callback?.handleMessage(Message.obtain(null, 1))
                }
                .create()
        }
        mAskOpenOtherAppDialog!!.show()
    }

    override fun onJsConfirm(
        view: android.webkit.WebView,
        url: String,
        message: String,
        jsResult: android.webkit.JsResult
    ) {
        onJsConfirmInternal(message, jsResult)
    }

    override fun onJsConfirm(
        view: com.tencent.smtt.sdk.WebView,
        url: String,
        message: String,
        jsResult: com.tencent.smtt.export.external.interfaces.JsResult
    ) {
        onJsConfirmInternal(message, jsResult)
    }

    override fun onSelectItemsPrompt(
        view: android.webkit.WebView,
        url: String,
        ways: Array<String>,
        callback: Handler.Callback
    ) {
        showChooserInternal(ways, callback)
    }

    override fun onSelectItemsPrompt(
        view: com.tencent.smtt.sdk.WebView,
        url: String,
        ways: Array<String>,
        callback: Handler.Callback
    ) {
        showChooserInternal(ways, callback)
    }

    override fun onForceDownloadAlert(url: String, callback: Handler.Callback) {
        onForceDownloadAlertInternal(callback)
    }

    private fun onForceDownloadAlertInternal(callback: Handler.Callback?) {
        val mActivity = this.mActivity
        if (mActivity == null || mActivity.isFinishing) {
            return
        }
        if (mAlertDialogForceDownload == null) {
            mAlertDialogForceDownload = AlertDialog.Builder(mActivity)
                .setTitle(mResources!!.getString(R.string.agentweb_tips))
                .setMessage(mResources!!.getString(R.string.agentweb_honeycomblow))
                .setNegativeButton(mResources!!.getString(R.string.agentweb_download)) { dialog, which ->
                    dialog?.dismiss()
                    callback?.handleMessage(Message.obtain())
                }//
                .setPositiveButton(mResources!!.getString(R.string.agentweb_cancel)) { dialog, which ->

                    dialog?.dismiss()
                }.create()
        }
        mAlertDialogForceDownload!!.show()
    }

    private fun showChooserInternal(ways: Array<String>, callback: Handler.Callback?) {
        if (mAlertDialog == null) {
            mAlertDialog = AlertDialog.Builder(mActivity!!)
                .setSingleChoiceItems(ways, -1) { dialog, which ->
                    dialog.dismiss()
                    Timber.i("which:$which")
                    if (callback != null) {
                        val mMessage = Message.obtain()
                        mMessage.what = which
                        callback.handleMessage(mMessage)
                    }

                }.setOnCancelListener { dialog ->
                    dialog.dismiss()
                    callback?.handleMessage(Message.obtain(null, -1))
                }.create()
        }
        mAlertDialog!!.show()
    }

    private fun onJsConfirmInternal(message: String, jsResult: android.webkit.JsResult) {
        Timber.i("activity:" + mActivity!!.hashCode() + "  ")
        val mActivity = this.mActivity
        if (mActivity == null || mActivity.isFinishing) {
            toCancelJsresult(jsResult)
            return
        }
        if (mConfirmDialog == null) {
            mConfirmDialog = AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel) { dialog, which ->
                    toDismissDialog(mConfirmDialog)
                    toCancelJsresult(mJsResult)
                }//
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    toDismissDialog(mConfirmDialog)
                    if (mJsResult != null) {
                        mJsResult!!.confirm()
                    }
                }
                .setOnCancelListener { dialog ->
                    dialog.dismiss()
                    toCancelJsresult(mJsResult)
                }
                .create()

        }
        mConfirmDialog!!.setMessage(message)
        this.mJsResult = jsResult
        mConfirmDialog!!.show()
    }

    private fun onJsConfirmInternal(message: String, jsResult: com.tencent.smtt.export.external.interfaces.JsResult) {
        Timber.i("activity:" + mActivity!!.hashCode() + "  ")
        val mActivity = this.mActivity
        if (mActivity == null || mActivity.isFinishing) {
            toCancelJsresult(jsResult)
            return
        }
        if (mConfirmDialog == null) {
            mConfirmDialog = AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel) { dialog, which ->
                    toDismissDialog(mConfirmDialog)
                    toCancelJsresult(mX5JsResult)
                }//
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    toDismissDialog(mConfirmDialog)
                    if (mX5JsResult != null) {
                        mX5JsResult!!.confirm()
                    }
                }
                .setOnCancelListener { dialog ->
                    dialog.dismiss()
                    toCancelJsresult(mX5JsResult)
                }
                .create()

        }
        mConfirmDialog!!.setMessage(message)
        this.mX5JsResult = jsResult
        mConfirmDialog!!.show()
    }

    private fun onJsPromptInternal(
        message: String,
        defaultValue: String,
        jsPromptResult: android.webkit.JsPromptResult
    ) {
        val mActivity = this.mActivity
        if (mActivity == null || mActivity.isFinishing) {
            jsPromptResult.cancel()
            return
        }
        if (mPromptDialog == null) {
            val et = EditText(mActivity)
            et.setText(defaultValue)
            mPromptDialog = AlertDialog.Builder(mActivity)
                .setView(et)
                .setTitle(message)
                .setNegativeButton(android.R.string.cancel) { dialog, which ->
                    toDismissDialog(mPromptDialog)
                    toCancelJsresult(mJsPromptResult)
                }//
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    toDismissDialog(mPromptDialog)
                    if (mJsPromptResult != null) {
                        mJsPromptResult!!.confirm(et.text.toString())
                    }
                }
                .setOnCancelListener { dialog ->
                    dialog.dismiss()
                    toCancelJsresult(mJsPromptResult)
                }
                .create()
        }
        this.mJsPromptResult = jsPromptResult
        mPromptDialog!!.show()
    }

    private fun onJsPromptInternal(
        message: String,
        defaultValue: String,
        jsPromptResult: com.tencent.smtt.export.external.interfaces.JsPromptResult
    ) {
        val mActivity = this.mActivity
        if (mActivity == null || mActivity.isFinishing) {
            jsPromptResult.cancel()
            return
        }
        if (mPromptDialog == null) {
            val et = EditText(mActivity)
            et.setText(defaultValue)
            mPromptDialog = AlertDialog.Builder(mActivity)
                .setView(et)
                .setTitle(message)
                .setNegativeButton(android.R.string.cancel) { dialog, which ->
                    toDismissDialog(mPromptDialog)
                    toCancelJsresult(mX5JsPromptResult)
                }//
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    toDismissDialog(mPromptDialog)
                    if (mX5JsPromptResult != null) {
                        mX5JsPromptResult!!.confirm(et.text.toString())
                    }
                }
                .setOnCancelListener { dialog ->
                    dialog.dismiss()
                    toCancelJsresult(mX5JsPromptResult)
                }
                .create()
        }
        this.mX5JsPromptResult = jsPromptResult
        mPromptDialog!!.show()
    }

    override fun onJsPrompt(
        view: android.webkit.WebView,
        url: String,
        message: String,
        defaultValue: String,
        jsPromptResult: android.webkit.JsPromptResult
    ) {
        onJsPromptInternal(message, defaultValue, jsPromptResult)
    }

    override fun onJsPrompt(
        view: com.tencent.smtt.sdk.WebView,
        url: String,
        message: String,
        defaultValue: String,
        jsPromptResult: com.tencent.smtt.export.external.interfaces.JsPromptResult
    ) {
        onJsPromptInternal(message, defaultValue, jsPromptResult)
    }

    override fun onMainFrameError(
        view: android.webkit.WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        Timber.i("mWebParentLayout onMainFrameError:" + mWebParentLayout!!)
        if (mWebParentLayout != null) {
            mWebParentLayout!!.showPageMainFrameError()
        }
    }

    override fun onMainFrameError(
        view: com.tencent.smtt.sdk.WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        Timber.i("mWebParentLayout onMainFrameError:" + mWebParentLayout!!)
        if (mWebParentLayout != null) {
            mWebParentLayout!!.showPageMainFrameError()
        }
    }

    override fun onShowMainFrame() {
        if (mWebParentLayout != null) {
            mWebParentLayout!!.hideErrorLayout()
        }
    }

    override fun onLoading(msg: String) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(mActivity)
        }
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.setCanceledOnTouchOutside(false)
        mProgressDialog!!.setMessage(msg)
        mProgressDialog!!.show()

    }

    override fun onCancelLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
        mProgressDialog = null
    }

    override fun onShowMessage(message: String, intent: String) {
        if (!TextUtils.isEmpty(intent) && intent.contains("performDownload")) {
            return
        }
        WebUtils.toastShowShort(mActivity!!.applicationContext, message)
    }

    override fun onPermissionsDeny(permissions: Array<String>, permissionType: String, action: String) {
        //		AgentWebUtils.toastShowShort(mActivity.getApplicationContext(), "权限被冻结");
    }

    private fun toCancelJsresult(result: android.webkit.JsResult?) {
        result?.cancel()
    }

    private fun toCancelJsresult(result: com.tencent.smtt.export.external.interfaces.JsResult?) {
        result?.cancel()
    }

    override fun bindSupportWebParent(webParentLayout: WebParentLayout, activity: Activity) {
        this.mActivity = activity
        this.mWebParentLayout = webParentLayout
        mResources = this.mActivity!!.resources
    }
}
