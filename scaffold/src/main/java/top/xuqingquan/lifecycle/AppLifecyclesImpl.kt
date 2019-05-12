package top.xuqingquan.lifecycle

import android.app.Application
import android.content.Context
import com.github.anrwatchdog.ANRWatchDog
import com.zxy.recovery.core.Recovery
import top.xuqingquan.BuildConfig
import top.xuqingquan.delegate.AppLifecycles
import top.xuqingquan.error.RecoveryCrashCallback
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019/4/15 00:26
 */
class AppLifecyclesImpl : AppLifecycles {

    override fun attachBaseContext(base: Context?) {
    }

    override fun onCreate(application: Application) {
        //需要再启用
//        AutoSizeConfig.getInstance().isCustomFragment = true
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            //崩溃重启框架，debug时使用
            Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .recoverEnabled(true)
                .callback(RecoveryCrashCallback())
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .init(application)
            //根据需要启用
//            Logger.addLogAdapter(object : AndroidLogAdapter(
//                PrettyFormatStrategy.newBuilder().tag(application.getString(R.string.app_name)).build()
//            ) {
//                override fun isLoggable(priority: Int, tag: String?): Boolean {
//                    return BuildConfig.DEBUG
//                }
//            })
            //ANR监视，debug时使用
            ANRWatchDog()
                .setIgnoreDebugger(true)
                .setANRListener {
                    it.printStackTrace()
                }
                .start()
        }
    }

    override fun onTerminate(application: Application) {
    }
}