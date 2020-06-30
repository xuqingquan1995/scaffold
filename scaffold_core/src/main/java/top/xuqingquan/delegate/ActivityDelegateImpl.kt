package top.xuqingquan.delegate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import org.greenrobot.eventbus.EventBus
import top.xuqingquan.stack.DebugStackDelegate
import top.xuqingquan.stack.DebugStackDelegateImpl
import top.xuqingquan.utils.haveAnnotation

/**
 * Created by 许清泉 on 2019/4/14 15:24
 * [Application.ActivityLifecycleCallbacks] 默认实现类
 * 通过 [ActivityDelegate] 管理 [Activity]
 */
class ActivityDelegateImpl(private var mActivity: Activity?) : ActivityDelegate {
    private var iActivity: IActivity? = null
    private var delegate: DebugStackDelegate? = null

    init {
        this.iActivity = mActivity as IActivity
        if (mActivity is FragmentActivity) {
            delegate = DebugStackDelegateImpl(mActivity as FragmentActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity!!.useEventBus() && haveAnnotation(mActivity!!)) {
            //注册到事件主线
            EventBus.getDefault().register(mActivity!!)
        }
        iActivity!!.setDebugStackDelegate(delegate)
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onSaveInstanceState(outState: Bundle?) {

    }

    override fun onDestroy() {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity != null && iActivity!!.useEventBus() && haveAnnotation(mActivity!!)) {
            EventBus.getDefault().unregister(mActivity)
        }
        this.delegate = null
        this.iActivity = null
        this.mActivity = null
    }
}
