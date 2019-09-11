package top.xuqingquan.webbak.system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.DownloadTask;
import com.download.library.Runtime;
import top.xuqingquan.R;
import top.xuqingquan.utils.NetUtils;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.webbak.nokernel.*;
import top.xuqingquan.webbak.publics.AbsAgentWebUIController;
import top.xuqingquan.webbak.publics.AgentWebConfig;
import top.xuqingquan.webbak.publics.AgentWebUtils;
import top.xuqingquan.webbak.publics.Extra;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 许清泉 on 2019-06-19 23:29
 */
public class DefaultDownloadImpl implements DownloadListener {
    /**
     * Application Context
     */
    private Context mContext;
    private ConcurrentHashMap<String, top.xuqingquan.webbak.nokernel.DownloadListener> mDownloadListeners = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Extra> mExtraServiceImpls = new ConcurrentHashMap<>();
    /**
     * Activity
     */
    private WeakReference<Activity> mActivityWeakReference = null;
    /**
     * TAG 用于打印，标识
     */
    private static final String TAG = DefaultDownloadImpl.class.getSimpleName();
    /**
     * 权限拦截
     */
    private PermissionInterceptor mPermissionListener = null;
    /**
     * AbsAgentWebUIController
     */
    private WeakReference<AbsAgentWebUIController> mAgentWebUIController;
    /**
     * Extra
     */
    private Extra mExtra;

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public DefaultDownloadImpl(Extra extra) {
        DownloadImpl.getInstance().with(extra.mActivity.getApplication());
        if (!extra.mIsCloneObject) {
            this.bind(extra);
            this.mExtra = extra;
        }
    }

    private void bind(Extra extra) {
        this.mActivityWeakReference = new WeakReference<>(extra.mActivity);
        this.mContext = extra.mActivity.getApplicationContext();
        if (extra.getDownloadListener() != null && !TextUtils.isEmpty(extra.getUrl())) {
            this.mDownloadListeners.put(extra.getUrl(), extra.getDownloadListener());
        }
        this.mPermissionListener = extra.mPermissionInterceptor;
        this.mAgentWebUIController = new WeakReference<>(AgentWebUtils.getAgentWebUIControllerByWebView(extra.mWebView));
    }


    @Override
    public void onDownloadStart(final String url, final String userAgent, final String contentDisposition, final String mimetype, final long contentLength) {
        mHandler.post(() -> onDownloadStartInternal(url, userAgent, contentDisposition, mimetype, contentLength, null));
    }

    private void onDownloadStartInternal(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, @SuppressWarnings("SameParameterValue") Extra extra) {
        if (null == mActivityWeakReference.get() || mActivityWeakReference.get().isFinishing()) {
            return;
        }
        if (null != this.mPermissionListener) {
            if (this.mPermissionListener.intercept(url, AgentWebPermissions.STORAGE, "download")) {
                return;
            }
        }
        Extra mCloneExtra;
        if (null == extra) {
            mCloneExtra = this.mExtra.clone();
        } else {
            mCloneExtra = extra;
        }
        mCloneExtra
                .setUrl(url)
                .setMimetype(mimetype)
                .setContentDisposition(contentDisposition)
                .setContentLength(contentLength)
                .setUserAgent(userAgent);
        this.mExtraServiceImpls.put(url, mCloneExtra);
        if (mCloneExtra.getDownloadListener() != null && !TextUtils.isEmpty(mCloneExtra.getUrl())) {
            this.mDownloadListeners.put(mCloneExtra.getUrl(), mCloneExtra.getDownloadListener());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> mList = checkNeedPermission();
            if (mList.isEmpty()) {
                preDownload(url);
            } else {
                Action mAction = Action.createPermissionsAction(mList.toArray(new String[]{}));
                ActionActivity.setPermissionListener(getPermissionListener(url));
                ActionActivity.start(mActivityWeakReference.get(), mAction);
            }
        } else {
            preDownload(url);
        }
    }

    private ActionActivity.PermissionListener getPermissionListener(final String url) {
        return (permissions, grantResults, extras) -> {
            if (checkNeedPermission().isEmpty()) {
                preDownload(url);
            } else {
                if (null != mAgentWebUIController.get()) {
                    mAgentWebUIController
                            .get()
                            .onPermissionsDeny(
                                    checkNeedPermission().
                                            toArray(new String[]{}),
                                    AgentWebPermissions.ACTION_STORAGE, "Download");
                }
                Timber.e("储存权限获取失败~");
            }

        };
    }

    private List<String> checkNeedPermission() {
        List<String> deniedPermissions = new ArrayList<>();
        if (!WebUtils.hasPermission(mActivityWeakReference.get(), AgentWebPermissions.STORAGE)) {
            deniedPermissions.addAll(Arrays.asList(AgentWebPermissions.STORAGE));
        }
        return deniedPermissions;
    }

    private void preDownload(String url) {
        Extra extraService = mExtraServiceImpls.get(url);
        //noinspection ConstantConditions
        DownloadTask downloadTask = extraService.getDownloadTask();
        top.xuqingquan.webbak.nokernel.DownloadListener downloadListener = mDownloadListeners.get(extraService.getUrl());
        // true 表示用户取消了该下载事件。
        if (null != downloadListener
                && downloadListener
                .onStart(extraService.getUrl(),
                        downloadTask.getUserAgent(),
                        downloadTask.getContentDisposition(),
                        downloadTask.getMimetype(),
                        downloadTask.getContentLength(),
                        extraService)) {
            return;
        }
        File file = Runtime.getInstance().uniqueFile(extraService.getDownloadTask(), new File(WebUtils.getAgentWebFilePath(mContext)));
        // File 创建文件失败
        if (null == file) {
            Timber.e("新建文件失败");
            return;
        }
        if (file.exists() && file.length() >= downloadTask.getContentLength() && downloadTask.getContentLength() > 0) {
            // true 用户处理了下载完成后续的通知用户事件
            if (null != downloadListener && downloadListener.onResult(null, Uri.fromFile(file), extraService.getUrl(), extraService)) {
                return;
            }
            Intent mIntent = WebUtils.getCommonFileIntentCompat(mContext, file);
            try {
//                mContext.getPackageManager().resolveActivity(mIntent)
                if (null != mIntent) {
                    if (!(mContext instanceof Activity)) {
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    mContext.startActivity(mIntent);
                }
                return;
            } catch (Throwable throwable) {
                Timber.e(throwable);
            }
            return;
        }
        downloadTask.setFile(file, downloadTask.getContext().getPackageName() + ".AgentWebFileProvider");
        // 移动数据
        if (!downloadTask.isForceDownload() &&
                NetUtils.checkNetworkType(mContext) > 1) {

            showDialog(url);
            return;
        }
        performDownload(url);
    }

    private void forceDownload(final String url) {
        Extra extraService = mExtraServiceImpls.get(url);
        //noinspection ConstantConditions
        extraService.setForceDownload(true);
        performDownload(url);
    }

    private void showDialog(final String url) {
        Activity mActivity;
        if (null == (mActivity = mActivityWeakReference.get()) || mActivity.isFinishing()) {
            return;
        }
        Extra extraService = mExtraServiceImpls.get(url);
        AbsAgentWebUIController mAgentWebUIController;
        if (null != (mAgentWebUIController = this.mAgentWebUIController.get())) {
            //noinspection ConstantConditions
            mAgentWebUIController.onForceDownloadAlert(extraService.getUrl(), createCallback(extraService.getUrl()));
        }
    }

    private Handler.Callback createCallback(final String url) {
        return msg -> {
            forceDownload(url);
            return true;
        };
    }

    private void performDownload(String url) {
        try {
            // 该链接是否正在下载
            if (DownloadImpl.getInstance().exist(url)) {
                if (null != mAgentWebUIController.get()) {
                    mAgentWebUIController.get().onShowMessage(
                            mActivityWeakReference.get()
                                    .getString(R.string.agentweb_download_task_has_been_exist),
                            TAG.concat("|preDownload"));
                }
                return;
            }
            Extra extraService = mExtraServiceImpls.get(url);
            //noinspection ConstantConditions
            DownloadTask downloadTask = extraService.getDownloadTask();
            if (null != mAgentWebUIController.get()) {
                mAgentWebUIController.get()
                        .onShowMessage(mActivityWeakReference.get().getString(R.string.agentweb_coming_soon_download) + ":" + downloadTask.getFile().getName(), TAG.concat("|performDownload"));
            }
            downloadTask.addHeader("Cookie", AgentWebConfig.getCookiesByUrl(url));
            downloadTask.setDownloadListenerAdapter(new WeakDownloadListener(mDownloadAdapter));
            DownloadImpl.getInstance().enqueue(downloadTask);
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    private DownloadListenerAdapter mDownloadAdapter = new DownloadListenerAdapter() {
        @Override
        public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, com.download.library.Extra extra) {
        }

        @Override
        public void onProgress(String url, long downloaded, long length, long useTime) {
            Timber.e(" downloaded:" + downloaded + " length:" + length + " url:" + url);
            top.xuqingquan.webbak.nokernel.DownloadListener downloadingListener = DefaultDownloadImpl.this.mDownloadListeners.get(url);
            if (null != downloadingListener) {
                downloadingListener.onProgress(url, downloaded, length, useTime);
            }
        }

        @Override
        public boolean onResult(Throwable throwable, Uri path, String url, com.download.library.Extra extra) {
            top.xuqingquan.webbak.nokernel.DownloadListener downloadListener = mDownloadListeners.remove(url);
            Extra wraper = mExtraServiceImpls.remove(url);
            return null != downloadListener && downloadListener.onResult(throwable, path, url, wraper);
        }
    };

    @SuppressWarnings("unused")
    private DownloadTask swrap(Extra extraService) {
        return extraService.getDownloadTask();
    }

    public static DefaultDownloadImpl create(@NonNull Activity activity,
                                             @NonNull WebView webView,
                                             @Nullable top.xuqingquan.webbak.nokernel.DownloadListener downloadListener,
                                             @Nullable PermissionInterceptor permissionInterceptor) {
        Extra extraService = new Extra()
                .setActivity(activity)
                .setWebView(webView)
                .setPermissionInterceptor(permissionInterceptor);
        extraService.setDownloadListener(downloadListener);
        return extraService.create();
    }

    private static class WeakDownloadListener extends DownloadListenerAdapter {
        private WeakReference<DownloadListenerAdapter> mDownloadListenerWeakReference;

        private WeakDownloadListener(DownloadListenerAdapter delegate) {
            mDownloadListenerWeakReference = new WeakReference<>(delegate);
        }

        @SuppressLint("WrongThread")
        @Override
        public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, com.download.library.Extra extra) {
            if (mDownloadListenerWeakReference.get() != null) {
                mDownloadListenerWeakReference.get().onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);
            }
        }

        @Override
        public void onProgress(String url, long downloaded, long length, long usedTime) {
            if (mDownloadListenerWeakReference.get() != null) {
                mDownloadListenerWeakReference.get().onProgress(url, downloaded, length, usedTime);
            }
        }

        @Override
        public boolean onResult(Throwable throwable, Uri path, String url, com.download.library.Extra extra) {
            if (mDownloadListenerWeakReference.get() != null) {
                return mDownloadListenerWeakReference.get().onResult(throwable, path, url, extra);
            }
            return false;
        }
    }
}