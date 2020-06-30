package top.xuqingquan.lifecycle

import android.app.Application
import android.content.Context
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener
import top.xuqingquan.delegate.AppLifecycle
import top.xuqingquan.utils.Timber
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
                Timber.d("QbSdk----Thread.currentThread()===${Thread.currentThread()}")
                // 在调用TBS初始化、创建WebView之前进行如下配置，以开启优化方案
                QbSdk.initTbsSettings(
                    mapOf(
                        TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true,
                        TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true
                    )
                )
                QbSdk.setDownloadWithoutWifi(true)
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
                val cb = object : QbSdk.PreInitCallback {
                    override fun onCoreInitFinished() {
                        Timber.d("QbSdk----onCoreInitFinished")

                    }

                    override fun onViewInitFinished(p0: Boolean) {
                        Timber.d("QbSdk----onViewInitFinished--->$p0")
                    }
                }
                //x5内核初始化接口
                QbSdk.initX5Environment(application, cb)
            }
        } catch (e: Throwable) {
        }
    }

    override fun onTerminate(application: Application) {
    }
}