package top.xuqingquan.lifecycle

import android.app.Application
import android.content.Context
import com.github.anrwatchdog.ANRWatchDog
import com.zxy.recovery.core.Recovery
import top.xuqingquan.delegate.AppLifecycle
import top.xuqingquan.error.RecoveryCrashCallback
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019/4/15 00:26
 */
class DebugLifecycleImpl : AppLifecycle {

    override fun attachBaseContext(base: Context?) {
    }

    override fun onCreate(application: Application) {
        Timber.plant(Timber.DebugTree())
        try {
            //ANR监视，debug时使用
            Class.forName("com.github.anrwatchdog.ANRWatchDog")
            ANRWatchDog()
                .setIgnoreDebugger(true)
                .setANRListener {
                    it.printStackTrace()
                }
                .start()
        } catch (_: Throwable) {
        }
        try {
            Class.forName("com.zxy.recovery.core.Recovery")
            //崩溃重启框架，debug时使用
            Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .recoverEnabled(true)
                .callback(RecoveryCrashCallback())
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .init(application)
        } catch (_: Throwable) {
        }
    }

    override fun onTerminate(application: Application) {
    }
}