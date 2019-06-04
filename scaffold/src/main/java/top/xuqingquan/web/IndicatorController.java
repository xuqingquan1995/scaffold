package top.xuqingquan.web;

import com.tencent.smtt.sdk.WebView;
import top.xuqingquan.web.agent.BaseIndicatorSpec;

public interface IndicatorController {

    void progress(WebView v, int newProgress);

    BaseIndicatorSpec offerIndicator();

    void showIndicator();

    void setProgress(int newProgress);

    void finish();
}
