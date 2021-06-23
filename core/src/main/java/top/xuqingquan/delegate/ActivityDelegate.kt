package top.xuqingquan.delegate

import android.app.Activity
import android.os.Bundle

/**
 * Created by 许清泉 on 2019/4/14 12:56
 * Activity的代理类
 */
interface ActivityDelegate {

    fun onCreate(activity: Activity, savedInstanceState: Bundle?)

    fun onStart(activity: Activity)

    fun onResume(activity: Activity)

    fun onPause(activity: Activity)

    fun onStop(activity: Activity)

    fun onDestroy(activity: Activity)

    fun onSaveInstanceState(activity: Activity, outState: Bundle?)

    companion object {
        const val ACTIVITY_DELEGATE = "ACTIVITY_DELEGATE"
    }
}