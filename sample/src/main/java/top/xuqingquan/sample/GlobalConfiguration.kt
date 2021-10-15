package top.xuqingquan.sample

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import top.xuqingquan.delegate.AppLifecycle
import top.xuqingquan.integration.LifecycleConfig
import top.xuqingquan.lifecycle.DebugLifecycleImpl

/**
 * Created by 许清泉 on 2019/4/15 00:33
 * 展示[LifecycleConfig] 用法，同时用作默认配置，避免开发者没有自定义出现崩溃
 */
class GlobalConfiguration : LifecycleConfig {

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycle>) {
        lifecycles.add(DebugLifecycleImpl())
    }

    override fun injectActivityLifecycle(
        context: Context,
        lifecycles: MutableList<Application.ActivityLifecycleCallbacks>
    ) {
        lifecycles.add(ActivityLifecycleCallbacksImpl())
    }

    override fun injectFragmentLifecycle(
        context: Context,
        lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
    ) {
//        lifecycles.add(FragmentLifecycleCallbacksImpl())
    }
}