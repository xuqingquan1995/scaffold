package top.xuqingquan.base.view.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

/**
 * Create by 许清泉 on 2020/6/9 18:57
 */

/**
 * 隐藏软键盘
 */
fun Activity.hideSoftKeyboard() {
    val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
    if (imm != null && imm.isActive) {
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}

/**
 * 显示软键盘
 * @param view 需要输入的组件
 * @param time 延迟时间，默认200毫秒
 */
fun Context.showSoftKeyboard(view: View, time: Long = 200L) {
    val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
    if (imm != null) {
        view.postDelayed({
            view.requestFocus()
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }, time)
    }
}