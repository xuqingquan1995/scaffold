package top.xuqingquan.web.x5;

import com.tencent.smtt.sdk.WebViewClient;

public class MiddlewareWebClientBase extends WebViewClientDelegate {
    private MiddlewareWebClientBase mMiddleWrareWebClientBase;

    @SuppressWarnings("unused")
    public MiddlewareWebClientBase(MiddlewareWebClientBase client) {
        super(client);
        this.mMiddleWrareWebClientBase = client;
    }

    @SuppressWarnings("WeakerAccess")
    public MiddlewareWebClientBase(WebViewClient client) {
        super(client);
    }

    @SuppressWarnings("WeakerAccess")
    public MiddlewareWebClientBase() {
        super(null);
    }

    public final MiddlewareWebClientBase next() {
        return this.mMiddleWrareWebClientBase;
    }

    @Override
    public final void setDelegate(WebViewClient delegate) {
        super.setDelegate(delegate);

    }

    @SuppressWarnings("UnusedReturnValue")
    public final MiddlewareWebClientBase enq(MiddlewareWebClientBase middleWrareWebClientBase) {
        setDelegate(middleWrareWebClientBase);
        this.mMiddleWrareWebClientBase = middleWrareWebClientBase;
        return middleWrareWebClientBase;
    }

}
