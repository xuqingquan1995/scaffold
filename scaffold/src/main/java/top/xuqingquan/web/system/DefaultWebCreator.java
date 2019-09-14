package top.xuqingquan.web.system;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import top.xuqingquan.R;
import top.xuqingquan.utils.Timber;
import top.xuqingquan.utils.ViewUtils;
import top.xuqingquan.web.nokernel.BaseIndicatorSpec;
import top.xuqingquan.web.nokernel.BaseIndicatorView;
import top.xuqingquan.web.nokernel.WebConfig;
import top.xuqingquan.web.nokernel.WebIndicator;
import top.xuqingquan.web.publics.WebParentLayout;

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
    private BaseIndicatorSpec mBaseIndicatorSpec;
    private android.webkit.WebView mWebView;
    private FrameLayout mFrameLayout = null;

    /**
     * 使用默认的进度条
     */
    public DefaultWebCreator(@NonNull Activity activity,
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

    /**
     * 关闭进度条
     */
    public DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, @Nullable android.webkit.WebView webView, top.xuqingquan.web.system.IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.mIsNeedDefaultProgress = false;
        this.mIndex = index;
        this.mLayoutParams = lp;
        this.mWebView = webView;
        this.mIWebLayout = webLayout;
    }

    /**
     * 自定义Indicator
     */
    public DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, BaseIndicatorView progressView, android.webkit.WebView webView, top.xuqingquan.web.system.IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.mIsNeedDefaultProgress = false;
        this.mIndex = index;
        this.mLayoutParams = lp;
        this.mProgressView = progressView;
        this.mWebView = webView;
        this.mIWebLayout = webLayout;
    }

    public void setWebView(android.webkit.WebView webView) {
        mWebView = webView;
    }

    @NonNull
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
    @NonNull
    public FrameLayout getWebParentLayout() {
        return mFrameLayout;
    }

    private ViewGroup createLayout() {
        Activity mActivity = this.mActivity;
        WebParentLayout mFrameLayout = new WebParentLayout(mActivity);
        mFrameLayout.setId(R.id.web_parent_layout_id);
        mFrameLayout.setBackgroundColor(Color.WHITE);
        FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(-1, -1);
        this.mWebView = createWebView();
        View target = mIWebLayout == null ? this.mWebView : webLayout();
        mFrameLayout.addView(target, mLayoutParams);
        mFrameLayout.bindWebView(this.mWebView);
        Timber.i("  instanceof  AgentWebView:" + (this.mWebView instanceof top.xuqingquan.web.system.AgentWebView));
        if (this.mWebView instanceof top.xuqingquan.web.system.AgentWebView) {
            WebConfig.WEBVIEW_TYPE = WebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE;
        }
        ViewStub mViewStub = new ViewStub(mActivity);
        mViewStub.setId(R.id.mainframe_error_viewsub_id);
        mFrameLayout.addView(mViewStub, new FrameLayout.LayoutParams(-1, -1));
        if (mIsNeedDefaultProgress) {
            FrameLayout.LayoutParams lp;
            WebIndicator mWebIndicator = new WebIndicator(mActivity);
            if (mHeight > 0) {
                lp = new FrameLayout.LayoutParams(-2, ViewUtils.dp2px(mActivity, mHeight));
            } else {
                lp = mWebIndicator.offerLayoutParams();
            }
            if (mColor != -1) {
                mWebIndicator.setColor(mColor);
            }
            lp.gravity = Gravity.TOP;
            this.mBaseIndicatorSpec = mWebIndicator;
            mFrameLayout.addView((View) this.mBaseIndicatorSpec, lp);
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
            WebConfig.WEBVIEW_TYPE = WebConfig.WEBVIEW_CUSTOM_TYPE;
        }
        this.mWebView = mWebView;
        return mIWebLayout.getLayout();
    }

    private android.webkit.WebView createWebView() {
        android.webkit.WebView mWebView;
        if (this.mWebView != null) {
            mWebView = this.mWebView;
            WebConfig.WEBVIEW_TYPE = WebConfig.WEBVIEW_CUSTOM_TYPE;
        } else if (WebConfig.IS_KITKAT_OR_BELOW_KITKAT) {
            mWebView = new top.xuqingquan.web.system.AgentWebView(mActivity);
            WebConfig.WEBVIEW_TYPE = WebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE;
        } else {
            mWebView = new android.webkit.WebView(mActivity);
            WebConfig.WEBVIEW_TYPE = WebConfig.WEBVIEW_DEFAULT_TYPE;
        }
        return mWebView;
    }

    @Override
    @NonNull
    public BaseIndicatorSpec offer() {
        return mBaseIndicatorSpec;
    }
}
