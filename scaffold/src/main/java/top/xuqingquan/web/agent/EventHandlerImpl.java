package top.xuqingquan.web.agent;

import android.view.KeyEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Created by 许清泉 on 2019-06-05 00:46
 */
public abstract class EventHandlerImpl implements IEventHandler {
    private EventInterceptor mEventInterceptor;

    public EventHandlerImpl(EventInterceptor mEventInterceptor) {
        this.mEventInterceptor = mEventInterceptor;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NotNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return back();
        }
        return false;
    }

    @Override
    public boolean back() {
        return this.mEventInterceptor != null && this.mEventInterceptor.event();
    }
}
