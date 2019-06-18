package top.xuqingquan.web.x5;

import com.tencent.smtt.sdk.WebChromeClient;

public class MiddlewareWebChromeBase extends WebChromeClientDelegate {

    private MiddlewareWebChromeBase mMiddlewareWebChromeBase;

    protected MiddlewareWebChromeBase(WebChromeClient webChromeClient) {
        super(webChromeClient);
    }

    protected MiddlewareWebChromeBase() {
        super(null);
    }

    @Override
    public final void setDelegate(WebChromeClient delegate) {
        super.setDelegate(delegate);
    }

    public final MiddlewareWebChromeBase enq(MiddlewareWebChromeBase middlewareWebChromeBase) {
        setDelegate(middlewareWebChromeBase);
        this.mMiddlewareWebChromeBase = middlewareWebChromeBase;
        return this.mMiddlewareWebChromeBase;
    }


    public final MiddlewareWebChromeBase next() {
        return this.mMiddlewareWebChromeBase;
    }

}
