package top.xuqingquan.sample

import android.app.Activity
import android.app.Application
import android.os.Bundle
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019/4/15 00:35
 * 展示[Application.ActivityLifecycleCallbacks]用法
 */
class ActivityLifecycleCallbacksImpl:Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        Timber.v("onActivityCreated---${activity?.javaClass?.name}")
    }

    override fun onActivityStarted(activity: Activity?) {
        Timber.v("onActivityStarted---${activity?.javaClass?.name}")
    }

    override fun onActivityResumed(activity: Activity?) {
        Timber.v("onActivityResumed---${activity?.javaClass?.name}")
    }

    override fun onActivityPaused(activity: Activity?) {
        Timber.v("onActivityPaused---${activity?.javaClass?.name}")
    }

    override fun onActivityStopped(activity: Activity?) {
        Timber.v("onActivityStopped---${activity?.javaClass?.name}")
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        Timber.v("onActivitySaveInstanceState---${activity?.javaClass?.name}")
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Timber.v("onActivityDestroyed---${activity?.javaClass?.name}")
    }

}