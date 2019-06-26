package top.xuqingquan.delegate

import android.app.Application
import android.content.Context

/**
 * Created by 许清泉 on 2019/4/14 14:01
 * 用于代理 Application 的生命周期
 */
interface AppLifecycles {
    fun attachBaseContext(base: Context?)

    fun onCreate(application: Application)

    fun onTerminate(application: Application)
}