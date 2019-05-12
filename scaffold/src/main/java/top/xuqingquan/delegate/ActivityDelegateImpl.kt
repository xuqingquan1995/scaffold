package top.xuqingquan.delegate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import org.greenrobot.eventbus.EventBus
import top.xuqingquan.app.AppComponentUtils
import top.xuqingquan.stack.DebugStackDelegate
import top.xuqingquan.stack.DebugStackDelegateImpl
import top.xuqingquan.utils.EventBusHelper

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
        if (iActivity!!.useEventBus() && EventBusHelper.haveAnnotation(mActivity!!)) {
            //注册到事件主线
            EventBus.getDefault().register(mActivity!!)
        }
        iActivity!!.setDebugStackDelegate(delegate)
        //这里提供 AppComponent 对象给 BaseActivity 的子类, 用于 Dagger2 的依赖注入
        iActivity!!.setupActivityComponent(AppComponentUtils.obtainAppComponentFromContext(mActivity!!))
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
        if (iActivity != null && iActivity!!.useEventBus() && EventBusHelper.haveAnnotation(mActivity!!)) {
            EventBus.getDefault().unregister(mActivity)
        }
        this.delegate = null
        this.iActivity = null
        this.mActivity = null
    }
}
