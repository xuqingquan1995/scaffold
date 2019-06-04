package top.xuqingquan.web;

import android.app.Activity;
import androidx.annotation.DrawableRes;
import com.download.library.DownloadTask;
import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.web.agent.PermissionInterceptor;
import top.xuqingquan.web.x5.DownloadListener;

import java.io.Serializable;

public class Extra implements Cloneable, Serializable {
    transient Activity mActivity;
    transient PermissionInterceptor mPermissionInterceptor;
    transient WebView mWebView;
    boolean mIsCloneObject = false;
    private DownloadListener mDownloadListener;
    private DownloadTask mDownloadTask;

    Extra() {
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

    public Extra setUserAgent(String userAgent) {
        mDownloadTask.setUserAgent(userAgent);
        return this;
    }

    public Extra addHeader(String key, String value) {
        mDownloadTask.addHeader(key, value);
        return this;
    }

    public Extra setForceDownload(boolean force) {
        mDownloadTask.setForceDownload(force);
        return this;
    }

    public long getContentLength() {
        return mDownloadTask.getContentLength();
    }

    Extra setContentLength(long contentLength) {
        mDownloadTask.setContentLength(contentLength);
        return this;
    }

    Extra setActivity(Activity activity) {
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

    DownloadTask getDownloadTask() {
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

    public Extra setWebView(WebView webView) {
        this.mWebView = webView;
        return this;
    }

    public Extra setDownloadListener(DownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
        return this;
    }

    protected void destroy() {
        this.mIsCloneObject = true;
        this.mActivity = null;
        this.mPermissionInterceptor = null;
        this.mWebView = null;
    }

    @Override
    protected Extra clone() {
        try {
            Extra extra = (Extra) super.clone();
            extra.mDownloadTask = this.mDownloadTask.clone();
            return extra;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Extra();
    }

    DefaultDownloadImpl create() {
        return new DefaultDownloadImpl(this);
    }

    public String getUrl() {
        return mDownloadTask.getUrl();
    }
}