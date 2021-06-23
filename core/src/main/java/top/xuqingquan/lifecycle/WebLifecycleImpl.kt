package top.xuqingquan.lifecycle

import android.app.Application
import android.content.Context
import top.xuqingquan.delegate.AppLifecycle
import top.xuqingquan.web.nokernel.initAdblock
import top.xuqingquan.web.nokernel.initTbs
import kotlin.concurrent.thread

/**
 * Created by 许清泉 on 2019/4/15 00:26
 */
class WebLifecycleImpl : AppLifecycle {

    override fun attachBaseContext(base: Context?) {
    }

    override fun onCreate(application: Application) {
        try {
            Class.forName("com.tencent.smtt.sdk.QbSdk")
            thread {
                initTbs(application)
            }
        } catch (e: Throwable) {
        }
        try {
            Class.forName("org.adblockplus.libadblockplus.android.settings.AdblockHelper")
            initAdblock(application)
        }catch (e:Throwable){
        }
    }

    override fun onTerminate(application: Application) {
    }
}