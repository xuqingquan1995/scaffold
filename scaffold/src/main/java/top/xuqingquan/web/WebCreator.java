package top.xuqingquan.web;

import android.widget.FrameLayout;
import com.tencent.smtt.sdk.WebView;

public interface WebCreator extends IWebIndicator {
    WebCreator create();

    WebView getWebView();

    FrameLayout getWebParentLayout();
}
