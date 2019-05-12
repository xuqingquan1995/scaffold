package top.xuqingquan.app

import android.content.Context
import androidx.core.util.Preconditions
import top.xuqingquan.di.component.AppComponent

/**
 * Created by 许清泉 on 2019/4/15 00:16
 */
object AppComponentUtils {
    @JvmStatic
    fun obtainAppComponentFromContext(context: Context): AppComponent {
        Preconditions.checkNotNull(context, "${Context::class.java.name} cannot be null")
        Preconditions.checkState(
            context.applicationContext is App,
            "${context.applicationContext.javaClass.name} must be implements ${App::class.java.name}"
        )
        return (context.applicationContext as App).getAppComponent()
    }
}