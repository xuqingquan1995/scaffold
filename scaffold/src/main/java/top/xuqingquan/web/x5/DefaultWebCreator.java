package top.xuqingquan.web.x5;

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
    private IWebLayout mX5IWebLayout;
    private BaseIndicatorSpec mBaseIndicatorSpec;
    private com.tencent.smtt.sdk.WebView mX5WebView;
    private FrameLayout mFrameLayout = null;

    /**
     * 使用默认的进度条
     */
    public DefaultWebCreator(
            @NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index,
            int color, int mHeight, com.tencent.smtt.sdk.WebView webView, IWebLayout webLayout) {
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
     */
    public DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, @Nullable com.tencent.smtt.sdk.WebView webView, IWebLayout webLayout) {
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
     */
    public DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, BaseIndicatorView progressView, com.tencent.smtt.sdk.WebView webView, IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.mIsNeedDefaultProgress = false;
        this.mIndex = index;
        this.mLayoutParams = lp;
        this.mProgressView = progressView;
        this.mX5WebView = webView;
        this.mX5IWebLayout = webLayout;
    }

    public void setWebView(com.tencent.smtt.sdk.WebView webView) {
        mX5WebView = webView;
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
    public com.tencent.smtt.sdk.WebView getWebView() {
        return mX5WebView;
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
        this.mX5WebView = createX5WebView();
        View target = mX5IWebLayout == null ? this.mX5WebView : x5WebLayout();
        mFrameLayout.addView(target, mLayoutParams);
        mFrameLayout.bindWebView(this.mX5WebView);
        Timber.i("  instanceof  AgentWebView:" + (this.mX5WebView instanceof AgentWebView));
        if (this.mX5WebView instanceof AgentWebView) {
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

    private View x5WebLayout() {
        com.tencent.smtt.sdk.WebView mWebView = mX5IWebLayout.getWebView();
        if (mWebView == null) {
            mWebView = createX5WebView();
            mX5IWebLayout.getLayout().addView(mWebView, -1, -1);
            Timber.i("add webview");
        } else {
            WebConfig.WEBVIEW_TYPE = WebConfig.WEBVIEW_CUSTOM_TYPE;
        }
        this.mX5WebView = mWebView;
        return mX5IWebLayout.getLayout();
    }

    private com.tencent.smtt.sdk.WebView createX5WebView() {
        com.tencent.smtt.sdk.WebView mWebView;
        if (this.mX5WebView != null) {
            mWebView = this.mX5WebView;
            WebConfig.WEBVIEW_TYPE = WebConfig.WEBVIEW_CUSTOM_TYPE;
        } else if (WebConfig.IS_KITKAT_OR_BELOW_KITKAT) {
            mWebView = new AgentWebView(mActivity);
            WebConfig.WEBVIEW_TYPE = WebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE;
        } else {
            mWebView = new LollipopFixedWebView(mActivity);
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
