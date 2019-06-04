package top.xuqingquan.web;

import android.widget.FrameLayout;
import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.web.agent.IWebIndicator;

public interface WebCreator extends IWebIndicator {
    WebCreator create();

    WebView getWebView();

    FrameLayout getWebParentLayout();
}
