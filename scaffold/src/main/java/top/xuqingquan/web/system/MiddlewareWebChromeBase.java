package top.xuqingquan.web.system;

import android.webkit.WebChromeClient;

public class MiddlewareWebChromeBase extends WebChromeClientDelegate {

    private MiddlewareWebChromeBase mMiddlewareWebChromeBase;

    @SuppressWarnings("WeakerAccess")
    public MiddlewareWebChromeBase(WebChromeClient webChromeClient) {
        super(webChromeClient);
    }

    @SuppressWarnings("WeakerAccess")
    public MiddlewareWebChromeBase() {
        super(null);
    }

    @Override
    public final void setDelegate(WebChromeClient delegate) {
        super.setDelegate(delegate);
    }

    @SuppressWarnings("UnusedReturnValue")
    public final MiddlewareWebChromeBase enq(MiddlewareWebChromeBase middlewareWebChromeBase) {
        setDelegate(middlewareWebChromeBase);
        this.mMiddlewareWebChromeBase = middlewareWebChromeBase;
        return this.mMiddlewareWebChromeBase;
    }


    public final MiddlewareWebChromeBase next() {
        return this.mMiddlewareWebChromeBase;
    }

}
