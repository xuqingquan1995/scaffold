package top.xuqingquan.delegate

import android.os.Bundle

/**
 * Created by 许清泉 on 2019/4/14 12:56
 * Activity的代理类
 */
interface ActivityDelegate {

    fun onCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()

    fun onSaveInstanceState(outState: Bundle?)

    companion object {
        const val ACTIVITY_DELEGATE = "ACTIVITY_DELEGATE"
    }
}