package top.xuqingquan.web.publics;

import android.app.Activity;
import androidx.annotation.DrawableRes;
import com.download.library.DownloadTask;
import top.xuqingquan.web.nokernel.DownloadListener;
import top.xuqingquan.web.nokernel.PermissionInterceptor;
import top.xuqingquan.web.nokernel.WebConfig;

import java.io.Serializable;

/**
 * Created by 许清泉 on 2019-06-19 23:26
 */
@SuppressWarnings("unused")
public class Extra implements Cloneable, Serializable {
    public transient Activity mActivity;
    public transient PermissionInterceptor mPermissionInterceptor;
    public transient android.webkit.WebView mWebView;
    public transient com.tencent.smtt.sdk.WebView mX5WebView;
    public boolean mIsCloneObject = false;
    private DownloadListener mDownloadListener;
    private DownloadTask mDownloadTask;

    public Extra() {
        mDownloadTask = new DownloadTask();
    }

    public Extra setUrl(String url) {
        mDownloadTask.setUrl(url);
        return this;
    }

    public Extra setMimetype(String mimetype) {
        mDownloadTask.setMimetype(mimetype);
        return this;
    }

    public Extra setContentDisposition(String contentDisposition) {
        mDownloadTask.setContentDisposition(contentDisposition);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Extra setUserAgent(String userAgent) {
        mDownloadTask.setUserAgent(userAgent);
        return this;
    }

    public Extra addHeader(String key, String value) {
        mDownloadTask.addHeader(key, value);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Extra setForceDownload(boolean force) {
        mDownloadTask.setForceDownload(force);
        return this;
    }

    public long getContentLength() {
        return mDownloadTask.getContentLength();
    }

    public Extra setContentLength(long contentLength) {
        mDownloadTask.setContentLength(contentLength);
        return this;
    }

    public Extra setActivity(Activity activity) {
        mActivity = activity;
        mDownloadTask.setContext(mActivity.getApplicationContext());
        return this;
    }

    public Extra setAutoOpen(boolean autoOpen) {
        mDownloadTask.setAutoOpen(autoOpen);
        return this;
    }

    public Extra setDownloadTimeOut(long downloadTimeOut) {
        mDownloadTask.setDownloadTimeOut(downloadTimeOut);
        return this;
    }

    public Extra setConnectTimeOut(int connectTimeOut) {
        mDownloadTask.setConnectTimeOut(connectTimeOut);
        return this;
    }

    public Extra setBlockMaxTime(int blockMaxTime) {
        mDownloadTask.setBlockMaxTime(blockMaxTime);
        return this;
    }

    public DownloadListener getDownloadListener() {
        return mDownloadListener;
    }

    public DownloadTask getDownloadTask() {
        return mDownloadTask;
    }

    public Extra setBreakPointDownload(boolean breakPointDownload) {
        mDownloadTask.setBreakPointDownload(breakPointDownload);
        return this;
    }

    public Extra setPermissionInterceptor(PermissionInterceptor permissionInterceptor) {
        mPermissionInterceptor = permissionInterceptor;
        return this;
    }

    public Extra setIcon(@DrawableRes int icon) {
        mDownloadTask.setIcon(icon);
        return this;
    }

    public Extra setParallelDownload(boolean parallelDownload) {
        mDownloadTask.setParallelDownload(parallelDownload);
        return this;
    }

    public Extra setEnableIndicator(boolean enableIndicator) {
        mDownloadTask.setEnableIndicator(enableIndicator);
        return this;
    }

    public Extra setWebView(android.webkit.WebView webView) {
        this.mWebView = webView;
        return this;
    }

    public Extra setWebView(com.tencent.smtt.sdk.WebView webView) {
        this.mX5WebView = webView;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Extra setDownloadListener(DownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
        return this;
    }

    protected void destroy() {
        this.mIsCloneObject = true;
        this.mActivity = null;
        this.mPermissionInterceptor = null;
        if (WebConfig.hasX5()) {
            this.mX5WebView = null;
        } else {
            this.mWebView = null;
        }
    }

    @Override
    public Extra clone() {
        try {
            Extra extra = (Extra) super.clone();
            extra.mDownloadTask = this.mDownloadTask.clone();
            return extra;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Extra();
    }

    public top.xuqingquan.web.system.DefaultDownloadImpl create() {
        return new top.xuqingquan.web.system.DefaultDownloadImpl(this);
    }
    public top.xuqingquan.web.x5.DefaultDownloadImpl createX5() {
        return new top.xuqingquan.web.x5.DefaultDownloadImpl(this);
    }

    public String getUrl() {
        return mDownloadTask.getUrl();
    }
}