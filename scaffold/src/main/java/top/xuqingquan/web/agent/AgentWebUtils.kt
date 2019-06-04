package top.xuqingquan.web.agent

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.os.EnvironmentCompat
import com.google.android.material.snackbar.Snackbar
import top.xuqingquan.utils.FileUtils
import top.xuqingquan.utils.Timber

import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by 许清泉 on 2019-06-05 00:08
 */
object AgentWebUtils {
    @JvmStatic
    fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale).toInt()
    }

    @JvmStatic
    fun getAgentWebFilePath(context: Context): String {
        if (!TextUtils.isEmpty(top.xuqingquan.web.x5.AgentWebConfig.AGENTWEB_FILE_PATH)) {
            return top.xuqingquan.web.x5.AgentWebConfig.AGENTWEB_FILE_PATH
        }
        val dir = getDiskExternalCacheDir(context)
        val mFile = File(dir, top.xuqingquan.web.x5.AgentWebConfig.FILE_CACHE_PATH)
        try {
            if (!mFile.exists()) {
                mFile.mkdirs()
            }
        } catch (throwable: Throwable) {
            Timber.i("create dir exception")
        }

        Timber.i("path:" + mFile.absolutePath + "  path:" + mFile.path)
        AgentWebConfig.AGENTWEB_FILE_PATH = mFile.absolutePath
        return mFile.absolutePath
    }

    @JvmStatic
    fun getDiskExternalCacheDir(context: Context): String? {
        val mFile = context.externalCacheDir ?: return null
        return if (Environment.MEDIA_MOUNTED == EnvironmentCompat.getStorageState(mFile)) {
            mFile.absolutePath
        } else null
    }

    @JvmStatic
    fun show(
        parent: View,
        text: CharSequence,
        duration: Int,
        @ColorInt textColor: Int,
        @ColorInt bgColor: Int,
        actionText: CharSequence?,
        @ColorInt actionTextColor: Int,
        listener: View.OnClickListener?
    ) {
        val spannableString = SpannableString(text)
        val colorSpan = ForegroundColorSpan(textColor)
        spannableString.setSpan(colorSpan, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val snackbarWeakReference = WeakReference(Snackbar.make(parent, spannableString, duration))
        val snackbar = snackbarWeakReference.get()
        val view = snackbar?.view
        view?.setBackgroundColor(bgColor)
        if (snackbar != null && actionText != null && actionText.isNotEmpty() && listener != null) {
            snackbar.setActionTextColor(actionTextColor)
            snackbar.setAction(actionText, listener)
        }
        snackbar?.show()
    }

    @JvmStatic
    fun isExistMethod(o: Any?, methodName: String, vararg clazzs: Class<*>): Method? {
        if (null == o) {
            return null
        }
        try {
            val clazz = o.javaClass
            val mMethod = clazz.getDeclaredMethod(methodName, *clazzs)
            mMethod.isAccessible = true
            return mMethod
        } catch (t: Throwable) {
            Timber.e(t)
        }

        return null
    }

    @Throws(IOException::class)
    @JvmStatic
    private fun createFileByName(context: Context, name: String, cover: Boolean): File? {
        val path = getAgentWebFilePath(context)
        if (TextUtils.isEmpty(path)) {
            return null
        }
        val mFile = File(path, name)
        if (mFile.exists()) {
            if (cover) {
                mFile.delete()
                mFile.createNewFile()
            }
        } else {
            mFile.createNewFile()
        }
        return mFile
    }

    @JvmStatic
    private fun setIntentDataAndType(
        context: Context,
        intent: Intent,
        type: String,
        file: File,
        writeAble: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(FileUtils.getUriFromFile(context, file), type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
    }

    @JvmStatic
    fun createImageFile(context: Context): File? {
        var mFile: File? = null
        try {
            val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
            val imageName = String.format("aw_%s.jpg", timeStamp)
            mFile = createFileByName(context, imageName, true)
        } catch (e: Throwable) {
            Timber.e(e)
        }

        return mFile
    }

    @JvmStatic
    fun getCommonFileIntentCompat(context: Context, file: File): Intent? {
        val mIntent = Intent().setAction(Intent.ACTION_VIEW)
        setIntentDataAndType(context, mIntent, FileUtils.getMIMEType(file), file, false)
        return mIntent
    }

    @JvmStatic
    fun getIntentCaptureCompat(context: Context, file: File): Intent {
        val mIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val mUri = FileUtils.getUriFromFile(context, file)
        mIntent.addCategory(Intent.CATEGORY_DEFAULT)
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        return mIntent
    }

    @JvmStatic
    val isUIThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()

    private var mToast: Toast? = null

    @SuppressLint("ShowToast")
    @JvmStatic
    fun toastShowShort(context: Context, msg: String) {
        if (mToast == null) {
            mToast = Toast.makeText(context.applicationContext, msg, Toast.LENGTH_SHORT)
        }
        mToast!!.setText(msg)
        mToast!!.show()
    }

    private var mHandler: Handler? = null

    @JvmStatic
    fun runInUiThread(runnable: Runnable) {
        if (mHandler == null) {
            mHandler = Handler(Looper.getMainLooper())
        }
        mHandler!!.post(runnable)
    }
}
