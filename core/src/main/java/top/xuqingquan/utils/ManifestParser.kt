package top.xuqingquan.utils

import android.content.Context
import android.content.pm.PackageManager
import top.xuqingquan.integration.LifecycleConfig
import java.util.ArrayList

/**
 * Created by 许清泉 on 2019/4/14 22:59
 */
internal class ManifestParser(private val context: Context) {
    companion object {
        private const val MODULE_VALUE = "LifecycleConfig"
    }

    fun parse(): List<LifecycleConfig> {
        val modules = ArrayList<LifecycleConfig>()
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            )
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (MODULE_VALUE == appInfo.metaData.getString(key)) {
                        modules.add(parseModule(key))
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Unable to find metadata to parse LifecycleConfig", e)
        }
        return modules
    }

    private fun parseModule(className: String): LifecycleConfig {
        val clazz: Class<*>
        try {
            clazz = Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Unable to find LifecycleConfig implementation", e)
        }
        val module: Any
        try {
            module = clazz.getDeclaredConstructor().newInstance()
        } catch (e: InstantiationException) {
            throw RuntimeException("Unable to instantiate LifecycleConfig implementation for $clazz", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Unable to instantiate LifecycleConfig implementation for $clazz", e)
        }
        if (module !is LifecycleConfig) {
            throw RuntimeException("Expected instanceof LifecycleConfig, but found: $module")
        }
        return module
    }
}