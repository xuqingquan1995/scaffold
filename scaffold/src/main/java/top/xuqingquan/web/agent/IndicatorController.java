package top.xuqingquan.web.agent;

public interface IndicatorController {

    void progress(android.webkit.WebView v, int newProgress);

    void progress(com.tencent.smtt.sdk.WebView v, int newProgress);

    BaseIndicatorSpec offerIndicator();

    void showIndicator();

    void setProgress(int newProgress);

    void finish();
}
