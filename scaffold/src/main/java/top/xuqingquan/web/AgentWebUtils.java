package top.xuqingquan.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.AppOpsManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;
import com.google.android.material.snackbar.Snackbar;
import top.xuqingquan.R;
import top.xuqingquan.utils.FileUtils;
import top.xuqingquan.utils.Timber;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import static top.xuqingquan.web.AgentWebConfig.AGENTWEB_FILE_PATH;
import static top.xuqingquan.web.AgentWebConfig.FILE_CACHE_PATH;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AgentWebUtils {

    private static Handler mHandler = null;

    private AgentWebUtils() {
        throw new UnsupportedOperationException("u can't init me");
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    static void clearWebView(WebView m) {
        if (m == null) {
            return;
        }
        if (!isUIThread()) {
            return;
        }
        m.loadUrl("about:blank");
        m.stopLoading();
        if (m.getHandler() != null) {
            m.getHandler().removeCallbacksAndMessages(null);
        }
        m.removeAllViews();
        ViewGroup mViewGroup = ((ViewGroup) m.getParent());
        if (mViewGroup != null) {
            mViewGroup.removeView(m);
        }
        m.setWebChromeClient(null);
        m.setWebViewClient(null);
        m.setTag(null);
        m.clearHistory();
        m.destroy();
    }

    public static String getAgentWebFilePath(Context context) {
        if (!TextUtils.isEmpty(AGENTWEB_FILE_PATH)) {
            return AGENTWEB_FILE_PATH;
        }
        String dir = getDiskExternalCacheDir(context);
        File mFile = new File(dir, FILE_CACHE_PATH);
        try {
            if (!mFile.exists()) {
                mFile.mkdirs();
            }
        } catch (Throwable throwable) {
            Timber.i("create dir exception");
        }
        Timber.i("path:" + mFile.getAbsolutePath() + "  path:" + mFile.getPath());
        AGENTWEB_FILE_PATH = mFile.getAbsolutePath();
        return AGENTWEB_FILE_PATH;
    }


    public static File createFileByName(Context context, String name, boolean cover) throws IOException {
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

    static String getDiskExternalCacheDir(Context context) {
        File mFile = context.getExternalCacheDir();
        if (mFile != null && Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(mFile))) {
            return mFile.getAbsolutePath();
        }
        return null;
    }

    private static WeakReference<Snackbar> snackbarWeakReference;

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
        snackbarWeakReference = new WeakReference<>(Snackbar.make(parent, spannableString, duration));
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
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
        return null;

    }

    static void clearWebViewAllCache(Context context, WebView webView) {
        try {
            AgentWebConfig.removeAllCookies(null);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            context.deleteDatabase("webviewCache.db");
            context.deleteDatabase("webview.db");
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            FileUtils.clearCacheFolder(new File(AgentWebConfig.getCachePath(context)), 0);
        } catch (Throwable t) {
            Timber.e(t);
        }
    }

    static void clearWebViewAllCache(Context context) {
        try {
            clearWebViewAllCache(context, new WebView(context.getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return mFile;
    }

    static Intent getIntentCaptureCompat(Context context, File file) {
        Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri mUri = FileUtils.getUriFromFile(context, file);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        return mIntent;
    }

    public static boolean isUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private static Toast mToast = null;

    static void toastShowShort(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
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

    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        return hasPermission(context, Arrays.asList(permissions));
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
            String op = AppOpsManagerCompat.permissionToOp(permission);
            if (TextUtils.isEmpty(op)) {
                continue;
            }
            result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
            if (result != AppOpsManagerCompat.MODE_ALLOWED) {
                return false;
            }
        }
        return true;
    }

    public static List<String> getDeniedPermissions(Activity activity, String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return null;
        }
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (!hasPermission(activity, permissions[i])) {
                deniedPermissions.add(permissions[i]);
            }
        }
        return deniedPermissions;
    }


    public static AbsAgentWebUIController getAgentWebUIControllerByWebView(WebView webView) {
        WebParentLayout mWebParentLayout = getWebParentLayoutByWebView(webView);
        return mWebParentLayout.provide();
    }

    //获取应用的名称
    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return packageManager.getApplicationLabel(applicationInfo).toString();
    }

    static WebParentLayout getWebParentLayoutByWebView(WebView webView) {
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

    public static void runInUiThread(Runnable runnable) {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        mHandler.post(runnable);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
            builder.build().openFileChooser();
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
