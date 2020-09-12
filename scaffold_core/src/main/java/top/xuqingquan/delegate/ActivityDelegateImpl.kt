package top.xuqingquan.delegate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import org.greenrobot.eventbus.EventBus
import top.xuqingquan.extension.hideSoftKeyboard
import top.xuqingquan.utils.haveAnnotation

/**
 * Created by 许清泉 on 2019/4/14 15:24
 * [Application.ActivityLifecycleCallbacks] 默认实现类
 * 通过 [ActivityDelegate] 管理 [Activity]
 */
class ActivityDelegateImpl(private var mActivity: Activity?) : ActivityDelegate {
    private var iActivity: IActivity? = null

    init {
        this.iActivity = mActivity as IActivity
    }

    override fun onCreate(activity: Activity, savedInstanceState: Bundle?) {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity!!.useEventBus() && haveAnnotation(mActivity!!)) {
            //注册到事件主线
            EventBus.getDefault().register(mActivity!!)
        }
    }

    override fun onStart(activity: Activity) {

    }

    override fun onResume(activity: Activity) {

    }

    override fun onPause(activity: Activity) {
        activity.hideSoftKeyboard()
    }

    override fun onStop(activity: Activity) {

    }

    override fun onSaveInstanceState(activity: Activity, outState: Bundle?) {

    }

    override fun onDestroy(activity: Activity) {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity != null && iActivity!!.useEventBus() && haveAnnotation(mActivity!!)) {
            EventBus.getDefault().unregister(mActivity)
        }
        this.iActivity = null
        this.mActivity = null
    }
}
