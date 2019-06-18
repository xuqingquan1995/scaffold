package top.xuqingquan.web.publics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.core.util.Pair;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;

import java.util.HashSet;
import java.util.Set;

public class VideoImpl implements IVideo, EventInterceptor {

    private Activity mActivity;
    private android.webkit.WebView mWebView;
    private com.tencent.smtt.sdk.WebView mx5WebView;
    private Set<Pair<Integer, Integer>> mFlags;
    private View mMoiveView = null;
    private ViewGroup mMoiveParentView = null;
    private android.webkit.WebChromeClient.CustomViewCallback mCallback;
    private IX5WebChromeClient.CustomViewCallback mx5Callback;

    public VideoImpl(Activity mActivity, android.webkit.WebView webView) {
        this.mActivity = mActivity;
        this.mWebView = webView;
        mFlags = new HashSet<>();
    }

    public VideoImpl(Activity mActivity, com.tencent.smtt.sdk.WebView webView) {
        this.mActivity = mActivity;
        this.mx5WebView = webView;
        mFlags = new HashSet<>();
    }

    @Override
    public void onShowCustomView(View view, android.webkit.WebChromeClient.CustomViewCallback callback) {
        Activity mActivity;
        if ((mActivity = this.mActivity) == null || mActivity.isFinishing()) {
            return;
        }
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Window mWindow = mActivity.getWindow();
        Pair<Integer, Integer> mPair;
        // 保存当前屏幕的状态
        if ((mWindow.getAttributes().flags & WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) == 0) {
            mPair = new Pair<>(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 0);
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mFlags.add(mPair);
        }
        if ((mWindow.getAttributes().flags & WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED) == 0) {
            mPair = new Pair<>(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, 0);
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            mFlags.add(mPair);
        }
        if (mMoiveView != null) {
            callback.onCustomViewHidden();
            return;
        }
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
        }
        if (mMoiveParentView == null) {
            FrameLayout mDecorView = (FrameLayout) mActivity.getWindow().getDecorView();
            mMoiveParentView = new FrameLayout(mActivity);
            mMoiveParentView.setBackgroundColor(Color.BLACK);
            mDecorView.addView(mMoiveParentView);
        }
        this.mCallback = callback;
        mMoiveParentView.addView(this.mMoiveView = view);
        mMoiveParentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        Activity mActivity;
        if ((mActivity = this.mActivity) == null || mActivity.isFinishing()) {
            return;
        }
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Window mWindow = mActivity.getWindow();
        Pair<Integer, Integer> mPair;
        // 保存当前屏幕的状态
        if ((mWindow.getAttributes().flags & WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) == 0) {
            mPair = new Pair<>(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 0);
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mFlags.add(mPair);
        }
        if ((mWindow.getAttributes().flags & WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED) == 0) {
            mPair = new Pair<>(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, 0);
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            mFlags.add(mPair);
        }
        if (mMoiveView != null) {
            callback.onCustomViewHidden();
            return;
        }
        if (mx5WebView != null) {
            mx5WebView.setVisibility(View.GONE);
        }
        if (mMoiveParentView == null) {
            FrameLayout mDecorView = (FrameLayout) mActivity.getWindow().getDecorView();
            mMoiveParentView = new FrameLayout(mActivity);
            mMoiveParentView.setBackgroundColor(Color.BLACK);
            mDecorView.addView(mMoiveParentView);
        }
        this.mx5Callback = callback;
        mMoiveParentView.addView(this.mMoiveView = view);
        mMoiveParentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideCustomView() {
        if (mMoiveView == null) {
            return;
        }
        if (mActivity != null && mActivity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (!mFlags.isEmpty()) {
            for (Pair<Integer, Integer> mPair : mFlags) {
                if (mActivity != null && mPair.first != null && mPair.second != null) {
                    mActivity.getWindow().setFlags(mPair.second, mPair.first);
                }
            }
            mFlags.clear();
        }
        mMoiveView.setVisibility(View.GONE);
        if (mMoiveParentView != null && mMoiveView != null) {
            mMoiveParentView.removeView(mMoiveView);

        }
        if (mMoiveParentView != null) {
            mMoiveParentView.setVisibility(View.GONE);
        }
        this.mMoiveView = null;
        if (AgentWebConfig.hasX5()) {
            if (this.mx5Callback != null) {
                mx5Callback.onCustomViewHidden();
            }
            if (mx5WebView != null) {
                mx5WebView.setVisibility(View.VISIBLE);
            }
        } else {
            if (this.mCallback != null) {
                mCallback.onCustomViewHidden();
            }
            if (mWebView != null) {
                mWebView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean isVideoState() {
        return mMoiveView != null;
    }

    @Override
    public boolean event() {
        if (isVideoState()) {
            onHideCustomView();
            return true;
        } else {
            return false;
        }
    }
}
