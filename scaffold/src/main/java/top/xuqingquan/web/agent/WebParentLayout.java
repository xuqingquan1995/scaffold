package top.xuqingquan.web.agent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import top.xuqingquan.R;
import top.xuqingquan.utils.Timber;

public class WebParentLayout extends FrameLayout implements Provider<AbsAgentWebUIController> {
    private AbsAgentWebUIController mAgentWebUIController = null;
    @LayoutRes
    private int mErrorLayoutRes;
    @IdRes
    private int mClickId = -1;
    private View mErrorView;
    private android.webkit.WebView mWebView;
    private com.tencent.smtt.sdk.WebView mX5WebView;
    private FrameLayout mErrorLayout = null;

    public WebParentLayout(@NonNull Context context) {
        this(context, null);
        Timber.i("WebParentLayout");
    }

    WebParentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    WebParentLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("WebParentLayout context must be activity or activity sub class .");
        }
        this.mErrorLayoutRes = R.layout.agentweb_error_page;
    }

    public void bindController(AbsAgentWebUIController agentWebUIController) {
        this.mAgentWebUIController = agentWebUIController;
        this.mAgentWebUIController.bindWebParent(this, (Activity) getContext());
    }

    public void showPageMainFrameError() {
        View container = this.mErrorLayout;
        if (container != null) {
            container.setVisibility(View.VISIBLE);
        } else {
            createErrorLayout();
            container = this.mErrorLayout;
        }
        View clickView = container.findViewById(mClickId);
        if (mClickId != -1 && clickView != null) {
            clickView.setClickable(true);
        } else {
            container.setClickable(true);
        }
    }

    private void createErrorLayout() {
        final FrameLayout mFrameLayout = new FrameLayout(getContext());
        mFrameLayout.setBackgroundColor(Color.WHITE);
        mFrameLayout.setId(R.id.mainframe_error_container_id);
        if (this.mErrorView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
            Timber.i("mErrorLayoutRes:" + mErrorLayoutRes);
            mLayoutInflater.inflate(mErrorLayoutRes, mFrameLayout, true);
        } else {
            mFrameLayout.addView(mErrorView);
        }
        ViewStub mViewStub = this.findViewById(R.id.mainframe_error_viewsub_id);
        final int index = this.indexOfChild(mViewStub);
        this.removeViewInLayout(mViewStub);
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            this.addView(this.mErrorLayout = mFrameLayout, index, layoutParams);
        } else {
            this.addView(this.mErrorLayout = mFrameLayout, index);
        }
        mFrameLayout.setVisibility(View.VISIBLE);
        if (mClickId != -1) {
            final View clickView = mFrameLayout.findViewById(mClickId);
            if (clickView != null) {
                clickView.setOnClickListener(v -> {
                    if (getWebView() != null) {
                        clickView.setClickable(false);
                        getWebView().reload();
                    }
                });
                return;
            } else {
                Timber.e("ClickView is null , cannot bind accurate view to refresh or reload .");
            }
        }
        mFrameLayout.setOnClickListener(v -> {
            if (getWebView() != null) {
                mFrameLayout.setClickable(false);
                getWebView().reload();
            }
        });
    }

    public void hideErrorLayout() {
        View mView = null;
        if ((mView = this.findViewById(R.id.mainframe_error_container_id)) != null) {
            mView.setVisibility(View.GONE);
        }
    }

    public void setErrorView(@NonNull View errorView) {
        this.mErrorView = errorView;
    }

    public void setErrorLayoutRes(@LayoutRes int resLayout, @IdRes int id) {
        this.mClickId = id;
        if (this.mClickId <= 0) {
            this.mClickId = -1;
        }
        this.mErrorLayoutRes = resLayout;
        if (this.mErrorLayoutRes <= 0) {
            this.mErrorLayoutRes = R.layout.agentweb_error_page;
        }
    }

    @Override
    public AbsAgentWebUIController provide() {
        return this.mAgentWebUIController;
    }

    public void bindWebView(android.webkit.WebView view) {
        if (this.mWebView == null) {
            this.mWebView = view;
        }
    }

    public android.webkit.WebView getWebView() {
        return this.mWebView;
    }

    public void bindWebView(com.tencent.smtt.sdk.WebView view) {
        if (this.mX5WebView == null) {
            this.mX5WebView = view;
        }
    }

    public com.tencent.smtt.sdk.WebView getX5WebView() {
        return this.mX5WebView;
    }

}
