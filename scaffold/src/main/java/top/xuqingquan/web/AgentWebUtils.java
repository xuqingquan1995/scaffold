package top.xuqingquan.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;
import androidx.annotation.ColorInt;
import androidx.core.os.EnvironmentCompat;
import com.google.android.material.snackbar.Snackbar;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.R;
import top.xuqingquan.utils.FileUtils;
import top.xuqingquan.utils.Timber;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AgentWebUtils {

    private static Handler mHandler = null;

    private AgentWebUtils() {
        throw new UnsupportedOperationException("u can't init me");
    }

    static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale);
    }

    static void clearWebView(WebView m) {
        if (m == null) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        m.loadUrl("about:blank");
        m.stopLoading();
        if (m.getHandler() != null) {
            m.getHandler().removeCallbacksAndMessages(null);
        }
        m.removeAllViews();
        ViewGroup mViewGroup;
        if ((mViewGroup = ((ViewGroup) m.getParent())) != null) {
            mViewGroup.removeView(m);
        }
        m.setWebChromeClient(null);
        m.setWebViewClient(null);
        m.setTag(null);
        m.clearHistory();
        m.destroy();
    }

    static String getAgentWebFilePath(Context context) {
        if (!TextUtils.isEmpty(AgentWebConfig.AGENTWEB_FILE_PATH)) {
            return AgentWebConfig.AGENTWEB_FILE_PATH;
        }
        String dir = getDiskExternalCacheDir(context);
        File mFile = new File(dir, AgentWebConfig.FILE_CACHE_PATH);
        try {
            if (!mFile.exists()) {
                mFile.mkdirs();
            }
        } catch (Throwable throwable) {
            Timber.i("create dir exception");
        }
        Timber.i("path:" + mFile.getAbsolutePath() + "  path:" + mFile.getPath());
        return AgentWebConfig.AGENTWEB_FILE_PATH = mFile.getAbsolutePath();
    }


    private static File createFileByName(Context context, String name, boolean cover) throws IOException {
        String path = getAgentWebFilePath(context);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File mFile = new File(path, name);
        if (mFile.exists()) {
            if (cover) {
                mFile.delete();
                mFile.createNewFile();
            }
        } else {
            mFile.createNewFile();
        }
        return mFile;
    }


    private static void setIntentDataAndType(Context context,
                                             Intent intent,
                                             String type,
                                             File file,
                                             boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(FileUtils.getUriFromFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }

    static String getDiskExternalCacheDir(Context context) {
        File mFile = context.getExternalCacheDir();
        if (mFile == null) {
            return null;
        }
        if (Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(mFile))) {
            return mFile.getAbsolutePath();
        }
        return null;
    }

    static void show(View parent,
                     CharSequence text,
                     int duration,
                     @ColorInt int textColor,
                     @ColorInt int bgColor,
                     CharSequence actionText,
                     @ColorInt int actionTextColor,
                     View.OnClickListener listener) {
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(textColor);
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        WeakReference<Snackbar> snackbarWeakReference = new WeakReference<>(Snackbar.make(parent, spannableString, duration));
        Snackbar snackbar = snackbarWeakReference.get();
        View view = snackbar.getView();
        view.setBackgroundColor(bgColor);
        if (actionText != null && actionText.length() > 0 && listener != null) {
            snackbar.setActionTextColor(actionTextColor);
            snackbar.setAction(actionText, listener);
        }
        snackbar.show();
    }

    static Method isExistMethod(Object o, String methodName, Class... clazzs) {
        if (null == o) {
            return null;
        }
        try {
            Class clazz = o.getClass();
            Method mMethod = clazz.getDeclaredMethod(methodName, clazzs);
            mMethod.setAccessible(true);
            return mMethod;
        } catch (Throwable ignore) {
            Timber.e(ignore);
        }
        return null;

    }

    static void clearWebViewAllCache(Context context, WebView webView) {
        try {
            AgentWebConfig.removeAllCookies(null);
            webView.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);
            context.deleteDatabase("webviewCache.db");
            context.deleteDatabase("webview.db");
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            FileUtils.clearCacheFolder(new File(AgentWebConfig.getCachePath(context)), 0);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    static void clearWebViewAllCache(Context context) {
        try {
            clearWebViewAllCache(context, new WebView(context.getApplicationContext()));
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    static File createImageFile(Context context) {
        File mFile = null;
        try {
            String timeStamp =
                    new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
            String imageName = String.format("aw_%s.jpg", timeStamp);
            mFile = createFileByName(context, imageName, true);
        } catch (Throwable e) {
            Timber.e(e);
        }
        return mFile;
    }

    public static Intent getCommonFileIntentCompat(Context context, File file) {
        Intent mIntent = new Intent().setAction(Intent.ACTION_VIEW);
        setIntentDataAndType(context, mIntent, FileUtils.getMIMEType(file), file, false);
        return mIntent;
    }

    static Intent getIntentCaptureCompat(Context context, File file) {
        Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri mUri = FileUtils.getUriFromFile(context, file);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        return mIntent;
    }

    static boolean isUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private static Toast mToast = null;

    static void toastShowShort(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

    @Deprecated
    static void getUIControllerAndShowMessage(Activity activity, String message, String from) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        WebParentLayout mWebParentLayout = activity.findViewById(R.id.web_parent_layout_id);
        AbsAgentWebUIController mAgentWebUIController = mWebParentLayout.provide();
        if (mAgentWebUIController != null) {
            mAgentWebUIController.onShowMessage(message, from);
        }
    }

    static AbsAgentWebUIController getAgentWebUIControllerByWebView(WebView webView) {
        WebParentLayout mWebParentLayout = getWebParentLayoutByWebView(webView);
        return mWebParentLayout.provide();
    }

    private static WebParentLayout getWebParentLayoutByWebView(WebView webView) {
        ViewGroup mViewGroup;
        if (!(webView.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException("please check webcreator's create method was be called ?");
        }
        mViewGroup = (ViewGroup) webView.getParent();
        while (mViewGroup != null) {

            Timber.i("ViewGroup:" + mViewGroup);
            if (mViewGroup.getId() == R.id.web_parent_layout_id) {
                WebParentLayout mWebParentLayout = (WebParentLayout) mViewGroup;
                Timber.i("found WebParentLayout");
                return mWebParentLayout;
            } else {
                ViewParent mViewParent = mViewGroup.getParent();
                if (mViewParent instanceof ViewGroup) {
                    mViewGroup = (ViewGroup) mViewParent;
                } else {
                    mViewGroup = null;
                }
            }
        }
        throw new IllegalStateException("please check webcreator's create method was be called ?");
    }

    static void runInUiThread(Runnable runnable) {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        mHandler.post(runnable);
    }

    static boolean showFileChooserCompat(Activity activity,
                                         WebView webView,
                                         ValueCallback<Uri[]> valueCallbacks,
                                         WebChromeClient.FileChooserParams fileChooserParams,
                                         PermissionInterceptor permissionInterceptor,
                                         ValueCallback<Uri> valueCallback,
                                         String mimeType,
                                         Handler.Callback jsChannelCallback
    ) {
        try {
            FileChooser.Builder builder = FileChooser.newBuilder(activity, webView);
            if (valueCallbacks != null) {
                builder.setUriValueCallbacks(valueCallbacks);
            }
            if (fileChooserParams != null) {
                builder.setFileChooserParams(fileChooserParams);
            }
            if (valueCallback != null) {
                builder.setUriValueCallback(valueCallback);
            }
            if (!TextUtils.isEmpty(mimeType)) {
                builder.setAcceptType(mimeType);
            }
            if (jsChannelCallback != null) {
                builder.setJsChannelCallback(jsChannelCallback);
            }
            builder.setPermissionInterceptor(permissionInterceptor);
            FileChooser fileChooser = builder.build();
            fileChooser.openFileChooser();
        } catch (Throwable throwable) {
            Timber.e(throwable);
            if (valueCallbacks != null) {
                Timber.i("onReceiveValue empty");
                return false;
            }
            if (valueCallback != null) {
                valueCallback.onReceiveValue(null);
            }
        }
        return true;
    }
}
