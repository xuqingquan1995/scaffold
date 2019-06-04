package top.xuqingquan.web.agent

import android.view.KeyEvent

/**
 * Created by 许清泉 on 2019-06-05 00:46
 */
abstract class EventHandlerImpl(private val mEventInterceptor: EventInterceptor?) : IEventHandler {

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            back()
        } else {
            false
        }
    }

    override fun back(): Boolean {
        return this.mEventInterceptor != null && this.mEventInterceptor.event()
    }
}
