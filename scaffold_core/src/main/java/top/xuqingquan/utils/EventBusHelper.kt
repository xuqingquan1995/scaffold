package top.xuqingquan.utils

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.EventBus

import java.lang.reflect.Method

/**
 * Created by 许清泉 on 2019-04-21 21:22
 */

/**
 * [EventBus] 要求注册之前, 订阅者必须含有一个或以上声明 [Subscribe]
 * 注解的方法, 否则会报错, 所以如果要想完成在基类中自动注册, 避免报错就要先检查是否符合注册资格
 *
 * @param subscriber 订阅者ç
 * @return 返回 `true` 则表示含有 [Subscribe] 注解, `false` 为不含有
 */
internal fun haveAnnotation(subscriber: Any): Boolean {
    var skipSuperClasses = false
    var clazz: Class<*>? = subscriber.javaClass
    //查找类中符合注册要求的方法, 直到Object类
    while (clazz != null && !isSystemClass(clazz.name) && !skipSuperClasses) {
        val allMethods: Array<Method> = try {
            clazz.declaredMethods
        } catch (th: Throwable) {
            try {
                clazz.methods
            } catch (th2: Throwable) {
                continue
            } finally {
                skipSuperClasses = true
            }
        }
        for (method in allMethods) {
            val parameterTypes = method.parameterTypes
            //查看该方法是否含有 Subscribe 注解
            if (method.isAnnotationPresent(Subscribe::class.java) && parameterTypes.size == 1) {
                return true
            }
        } //end for
        //获取父类, 以继续查找父类中符合要求的方法
        clazz = clazz.superclass
    }
    return false
}

private fun isSystemClass(name: String): Boolean {
    return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.") || name.startsWith(
        "androidx."
    )
}