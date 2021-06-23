package top.xuqingquan.utils

import android.view.KeyEvent

/**
 * Created by 许清泉 on 2019-05-14 22:39
 */
interface FragmentOnKeyListener {
    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean?
}