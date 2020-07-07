package top.xuqingquan.lifecycle

import android.app.Application
import android.content.Context
import com.tencent.smtt.sdk.QbSdk
import top.xuqingquan.delegate.AppLifecycle
import top.xuqingquan.utils.Timber
import top.xuqingquan.web.nokernel.WebConfig
import top.xuqingquan.web.nokernel.initTbs
import kotlin.concurrent.thread

/**
 * Created by 许清泉 on 2019/4/15 00:26
 */
class X5LifecycleImpl : AppLifecycle {

    override fun attachBaseContext(base: Context?) {
    }

    override fun onCreate(application: Application) {
        try {
            Class.forName("com.tencent.smtt.sdk.QbSdk")
            thread {
                initTbs(application, true, object : QbSdk.PreInitCallback {
                    override fun onCoreInitFinished() {
                        Timber.d("QbSdk----onCoreInitFinished")

                    }

                    override fun onViewInitFinished(p0: Boolean) {
                        Timber.d("QbSdk----onViewInitFinished--->$p0")
                        //自定义tbs监听时，需要将初始化状态提供给WebConfig.x5
                        WebConfig.x5 = p0
                    }
                })
            }
        } catch (e: Throwable) {
        }
    }

    override fun onTerminate(application: Application) {
    }
}