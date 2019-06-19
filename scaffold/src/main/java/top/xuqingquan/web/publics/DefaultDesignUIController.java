package top.xuqingquan.web.publics;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import top.xuqingquan.utils.Timber;

public class DefaultDesignUIController extends DefaultUIController {

    private BottomSheetDialog mBottomSheetDialog;
    private static final int RECYCLERVIEW_ID = 0x1001;
    private Activity mActivity = null;
    private WebParentLayout mWebParentLayout;
    private LayoutInflater mLayoutInflater;

    @Override
    public void onJsAlert(android.webkit.WebView view, String url, String message) {
        onJsAlertInternal(view, message);
    }

    public void onJsAlert(com.tencent.smtt.sdk.WebView view, String url, String message) {
        onJsAlertInternal(view, message);
    }

    private void onJsAlertInternal(android.webkit.WebView view, String message) {
        Activity mActivity = this.mActivity;
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        try {
            AgentWebUtils.show(view,
                    message,
                    Snackbar.LENGTH_SHORT,
                    Color.WHITE,
                    mActivity.getResources().getColor(android.R.color.black),
                    null,
                    -1,
                    null);
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    private void onJsAlertInternal(com.tencent.smtt.sdk.WebView view, String message) {
        Activity mActivity = this.mActivity;
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        try {
            AgentWebUtils.show(view,
                    message,
                    Snackbar.LENGTH_SHORT,
                    Color.WHITE,
                    mActivity.getResources().getColor(android.R.color.black),
                    null,
                    -1,
                    null);
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    @Override
    public void onSelectItemsPrompt(android.webkit.WebView view, String url, String[] ways, Handler.Callback callback) {
        showChooserInternal(url, ways, callback);
    }

    @Override
    public void onSelectItemsPrompt(com.tencent.smtt.sdk.WebView view, String url, String[] ways, Handler.Callback callback) {
        showChooserInternal(url, ways, callback);
    }

    private void showChooserInternal(String url, final String[] ways, final Handler.Callback callback) {
        Timber.i("url:" + url + "  ways:" + ways[0]);
        RecyclerView mRecyclerView;
        if (mBottomSheetDialog == null) {
            mBottomSheetDialog = new BottomSheetDialog(mActivity);
            mRecyclerView = new RecyclerView(mActivity);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            mRecyclerView.setId(RECYCLERVIEW_ID);
            mBottomSheetDialog.setContentView(mRecyclerView);
        }
        mRecyclerView = mBottomSheetDialog.getDelegate().findViewById(RECYCLERVIEW_ID);
        if (mRecyclerView == null) {
            return;
        }
        mRecyclerView.setAdapter(getAdapter(ways, callback));
        mBottomSheetDialog.setOnCancelListener(dialog -> {
            if (callback != null) {
                callback.handleMessage(Message.obtain(null, -1));
            }
        });
        mBottomSheetDialog.show();
    }

    private RecyclerView.Adapter getAdapter(final String[] ways, final Handler.Callback callback) {
        return new RecyclerView.Adapter<BottomSheetHolder>() {
            @NonNull
            @Override
            public BottomSheetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new BottomSheetHolder(mLayoutInflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull BottomSheetHolder bottomSheetHolder, final int i) {
                TypedValue outValue = new TypedValue();
                mActivity.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                bottomSheetHolder.mTextView.setBackgroundResource(outValue.resourceId);
                bottomSheetHolder.mTextView.setText(ways[i]);
                bottomSheetHolder.mTextView.setOnClickListener(v -> {
                    if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
                        mBottomSheetDialog.dismiss();
                    }
                    Message mMessage = Message.obtain();
                    mMessage.what = i;
                    callback.handleMessage(mMessage);
                });
            }

            @Override
            public int getItemCount() {
                return ways.length;
            }
        };
    }

    private static class BottomSheetHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public BottomSheetHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(android.R.id.text1);
        }
    }

    @Override
    public void bindSupportWebParent(WebParentLayout webParentLayout, Activity activity) {
        super.bindSupportWebParent(webParentLayout, activity);
        this.mActivity = activity;
        this.mWebParentLayout = webParentLayout;
        mLayoutInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public void onShowMessage(String message, String from) {
        if (!TextUtils.isEmpty(from) && from.contains("performDownload")) {
            return;
        }
        if (AgentWebConfig.hasX5()) {
            onJsAlertInternal(mWebParentLayout.getX5WebView(), message);
        } else {
            onJsAlertInternal(mWebParentLayout.getWebView(), message);
        }
    }
}
