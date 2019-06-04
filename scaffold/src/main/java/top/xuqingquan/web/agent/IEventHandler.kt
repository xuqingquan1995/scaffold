package top.xuqingquan.web.agent

import android.view.KeyEvent

interface IEventHandler {

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean

    fun back(): Boolean
}
