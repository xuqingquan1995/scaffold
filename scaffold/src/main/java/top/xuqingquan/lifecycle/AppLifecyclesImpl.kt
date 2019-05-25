package top.xuqingquan.lifecycle

import android.app.Application
import android.content.Context
import com.github.anrwatchdog.ANRWatchDog
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener
import com.zxy.recovery.core.Recovery
import me.jessyan.autosize.AutoSizeConfig
import top.xuqingquan.BuildConfig
import top.xuqingquan.delegate.AppLifecycles
import top.xuqingquan.error.RecoveryCrashCallback
import top.xuqingquan.utils.Timber
import kotlin.concurrent.thread

/**
 * Created by 许清泉 on 2019/4/15 00:26
 */
class AppLifecyclesImpl : AppLifecycles {

    override fun attachBaseContext(base: Context?) {
    }

    override fun onCreate(application: Application) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
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
        } catch (e: Throwable) {
            Timber.e(e)
        }
        try {
            Class.forName("me.jessyan.autosize.AutoSizeConfig")
            AutoSizeConfig.getInstance().isCustomFragment = true
        } catch (e: Throwable) {
            Timber.e(e)
        }
        try {
            Class.forName("com.tencent.smtt.sdk.QbSdk")
            thread {
                Timber.d("QbSdk----Thread.currentThread()===${Thread.currentThread()}")
                QbSdk.setDownloadWithoutWifi(true)
                val cb = object : QbSdk.PreInitCallback {
                    override fun onCoreInitFinished() {
                        Timber.d("QbSdk----onCoreInitFinished")

                    }

                    override fun onViewInitFinished(p0: Boolean) {
                        Timber.d("QbSdk----onViewInitFinished--->$p0")
                    }
                }
                QbSdk.setTbsListener(object : TbsListener {
                    override fun onInstallFinish(p0: Int) {
                        Timber.d("QbSdk----onInstallFinish--->$p0")
                    }

                    override fun onDownloadFinish(p0: Int) {
                        Timber.d("QbSdk----onDownloadFinish--->$p0")
                    }

                    override fun onDownloadProgress(p0: Int) {
                        Timber.d("QbSdk----onDownloadProgress--->$p0")
                    }
                })
                //x5内核初始化接口
                QbSdk.initX5Environment(application, cb)
            }
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }

    override fun onTerminate(application: Application) {
    }
}