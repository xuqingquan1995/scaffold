package top.xuqingquan.web;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.R;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.web.agent.*;

public class DefaultWebCreator implements WebCreator {
    private Activity mActivity;
    private ViewGroup mViewGroup;
    private boolean mIsNeedDefaultProgress;
    private int mIndex;
    private BaseIndicatorView mProgressView;
    private ViewGroup.LayoutParams mLayoutParams;
    private int mColor = -1;
    /**
     * 单位dp
     */
    private int mHeight;
    private boolean mIsCreated = false;
    private top.xuqingquan.web.system.IWebLayout mIWebLayout;
    private top.xuqingquan.web.x5.IWebLayout mX5IWebLayout;
    private BaseIndicatorSpec mBaseIndicatorSpec;
    private android.webkit.WebView mWebView;
    private com.tencent.smtt.sdk.WebView mX5WebView;
    private FrameLayout mFrameLayout = null;

    /**
     * 使用默认的进度条
     *
     * @param activity
     * @param viewGroup
     * @param lp
     * @param index
     * @param color
     * @param mHeight
     * @param webView
     * @param webLayout
     */
    protected DefaultWebCreator(@NonNull Activity activity,
                                @Nullable ViewGroup viewGroup,
                                ViewGroup.LayoutParams lp,
                                int index,
                                int color,
                                int mHeight,
                                android.webkit.WebView webView,
                                top.xuqingquan.web.system.IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.mIsNeedDefaultProgress = true;
        this.mIndex = index;
        this.mColor = color;
        this.mLayoutParams = lp;
        this.mHeight = mHeight;
        this.mWebView = webView;
        this.mIWebLayout = webLayout;
    }

    protected DefaultWebCreator(@NonNull Activity activity,
                                @Nullable ViewGroup viewGroup,
                                ViewGroup.LayoutParams lp,
                                int index,
                                int color,
                                int mHeight,
                                com.tencent.smtt.sdk.WebView webView,
                                top.xuqingquan.web.x5.IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.mIsNeedDefaultProgress = true;
        this.mIndex = index;
        this.mColor = color;
        this.mLayoutParams = lp;
        this.mHeight = mHeight;
        this.mX5WebView = webView;
        this.mX5IWebLayout = webLayout;
    }

    /**
     * 关闭进度条
     *
     * @param activity
     * @param viewGroup
     * @param lp
     * @param index
     * @param webView
     * @param webLayout
     */
    protected DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, @Nullable android.webkit.WebView webView, top.xuqingquan.web.system.IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.mIsNeedDefaultProgress = false;
        this.mIndex = index;
        this.mLayoutParams = lp;
        this.mWebView = webView;
        this.mIWebLayout = webLayout;
    }

    protected DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, @Nullable com.tencent.smtt.sdk.WebView webView, top.xuqingquan.web.x5.IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.mIsNeedDefaultProgress = false;
        this.mIndex = index;
        this.mLayoutParams = lp;
        this.mX5WebView = webView;
        this.mX5IWebLayout = webLayout;
    }

    /**
     * 自定义Indicator
     *
     * @param activity
     * @param viewGroup
     * @param lp
     * @param index
     * @param progressView
     * @param webView
     * @param webLayout
     */
    protected DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, BaseIndicatorView progressView, android.webkit.WebView webView, top.xuqingquan.web.system.IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.mIsNeedDefaultProgress = false;
        this.mIndex = index;
        this.mLayoutParams = lp;
        this.mProgressView = progressView;
        this.mWebView = webView;
        this.mIWebLayout = webLayout;
    }

    protected DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, BaseIndicatorView progressView, com.tencent.smtt.sdk.WebView webView, top.xuqingquan.web.x5.IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.mIsNeedDefaultProgress = false;
        this.mIndex = index;
        this.mLayoutParams = lp;
        this.mProgressView = progressView;
        this.mX5WebView = webView;
        this.mX5IWebLayout = webLayout;
    }


    public void setWebView(android.webkit.WebView webView) {
        mWebView = webView;
    }

    public void setWebView(com.tencent.smtt.sdk.WebView webView) {
        mX5WebView = webView;
    }

    @Override
    public DefaultWebCreator create() {
        if (mIsCreated) {
            return this;
        }
        mIsCreated = true;
        ViewGroup mViewGroup = this.mViewGroup;
        this.mFrameLayout = (FrameLayout) createLayout();
        if (mViewGroup == null) {
            mViewGroup = this.mFrameLayout;
            mActivity.setContentView(mViewGroup);
        } else {
            if (mIndex == -1) {
                mViewGroup.addView(this.mFrameLayout, mLayoutParams);
            } else {
                mViewGroup.addView(this.mFrameLayout, mIndex, mLayoutParams);
            }
        }
        return this;
    }

    @Override
    public android.webkit.WebView getWebView() {
        return mWebView;
    }

    @Override
    public WebView getX5WebView() {
        return mX5WebView;
    }

    @Override
    public FrameLayout getWebParentLayout() {
        return mFrameLayout;
    }

    private ViewGroup createLayout() {
        Activity mActivity = this.mActivity;
        WebParentLayout mFrameLayout = new WebParentLayout(mActivity);
        mFrameLayout.setId(R.id.web_parent_layout_id);
        mFrameLayout.setBackgroundColor(Color.WHITE);
        FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(-1, -1);
        if (AgentWebConfig.hasX5()) {
            this.mX5WebView = createX5WebView();
            View target = mX5IWebLayout == null ? this.mX5WebView : webLayout();
            mFrameLayout.addView(target, mLayoutParams);
            mFrameLayout.bindWebView(this.mX5WebView);
            Timber.i("  instanceof  AgentWebView:" + (this.mX5WebView instanceof top.xuqingquan.web.x5.AgentWebView));
            if (this.mX5WebView instanceof top.xuqingquan.web.x5.AgentWebView) {
                AgentWebConfig.WEBVIEW_TYPE = AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE;
            }
        } else {
            this.mWebView = createWebView();
            View target = mIWebLayout == null ? this.mWebView : webLayout();
            mFrameLayout.addView(target, mLayoutParams);
            mFrameLayout.bindWebView(this.mWebView);
            Timber.i("  instanceof  AgentWebView:" + (this.mWebView instanceof top.xuqingquan.web.system.AgentWebView));
            if (this.mWebView instanceof top.xuqingquan.web.system.AgentWebView) {
                AgentWebConfig.WEBVIEW_TYPE = AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE;
            }
        }
        ViewStub mViewStub = new ViewStub(mActivity);
        mViewStub.setId(R.id.mainframe_error_viewsub_id);
        mFrameLayout.addView(mViewStub, new FrameLayout.LayoutParams(-1, -1));
        if (mIsNeedDefaultProgress) {
            FrameLayout.LayoutParams lp;
            WebIndicator mWebIndicator = new WebIndicator(mActivity);
            if (mHeight > 0) {
                lp = new FrameLayout.LayoutParams(-2, AgentWebUtils.dp2px(mActivity, mHeight));
            } else {
                lp = mWebIndicator.offerLayoutParams();
            }
            if (mColor != -1) {
                mWebIndicator.setColor(mColor);
            }
            lp.gravity = Gravity.TOP;
            mFrameLayout.addView((View) (this.mBaseIndicatorSpec = mWebIndicator), lp);
            mWebIndicator.setVisibility(View.GONE);
        } else if (mProgressView != null) {
            this.mBaseIndicatorSpec = mProgressView;
            mFrameLayout.addView((View) (this.mBaseIndicatorSpec), mProgressView.offerLayoutParams());
            mProgressView.setVisibility(View.GONE);
        }
        return mFrameLayout;
    }

    private View webLayout() {
        android.webkit.WebView mWebView = mIWebLayout.getWebView();
        if (mWebView == null) {
            mWebView = createWebView();
            mIWebLayout.getLayout().addView(mWebView, -1, -1);
            Timber.i("add webview");
        } else {
            AgentWebConfig.WEBVIEW_TYPE = AgentWebConfig.WEBVIEW_CUSTOM_TYPE;
        }
        this.mWebView = mWebView;
        return mIWebLayout.getLayout();
    }

    private View x5WebLayout() {
        com.tencent.smtt.sdk.WebView mWebView = mX5IWebLayout.getWebView();
        if (mWebView == null) {
            mWebView = createX5WebView();
            mX5IWebLayout.getLayout().addView(mWebView, -1, -1);
            Timber.i("add webview");
        } else {
            AgentWebConfig.WEBVIEW_TYPE = AgentWebConfig.WEBVIEW_CUSTOM_TYPE;
        }
        this.mX5WebView = mWebView;
        return mX5IWebLayout.getLayout();
    }

    private android.webkit.WebView createWebView() {
        android.webkit.WebView mWebView;
        if (this.mWebView != null) {
            mWebView = this.mWebView;
            AgentWebConfig.WEBVIEW_TYPE = AgentWebConfig.WEBVIEW_CUSTOM_TYPE;
        } else if (AgentWebConfig.IS_KITKAT_OR_BELOW_KITKAT) {
            mWebView = new top.xuqingquan.web.system.AgentWebView(mActivity);
            AgentWebConfig.WEBVIEW_TYPE = AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE;
        } else {
            mWebView = new android.webkit.WebView(mActivity);
            AgentWebConfig.WEBVIEW_TYPE = AgentWebConfig.WEBVIEW_DEFAULT_TYPE;
        }
        return mWebView;
    }

    private com.tencent.smtt.sdk.WebView createX5WebView() {
        com.tencent.smtt.sdk.WebView mWebView;
        if (this.mX5WebView != null) {
            mWebView = this.mX5WebView;
            AgentWebConfig.WEBVIEW_TYPE = AgentWebConfig.WEBVIEW_CUSTOM_TYPE;
        } else if (AgentWebConfig.IS_KITKAT_OR_BELOW_KITKAT) {
            mWebView = new top.xuqingquan.web.x5.AgentWebView(mActivity);
            AgentWebConfig.WEBVIEW_TYPE = AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE;
        } else {
            mWebView = new com.tencent.smtt.sdk.WebView(mActivity);
            AgentWebConfig.WEBVIEW_TYPE = AgentWebConfig.WEBVIEW_DEFAULT_TYPE;
        }
        return mWebView;
    }

    @Override
    public BaseIndicatorSpec offer() {
        return mBaseIndicatorSpec;
    }
}
