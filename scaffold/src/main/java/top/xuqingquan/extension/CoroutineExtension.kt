package top.xuqingquan.extension

import android.arch.lifecycle.MutableLiveData
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import top.xuqingquan.BuildConfig
import kotlin.coroutines.CoroutineContext

/**
 * Create by 许清泉 on 2020/6/9 18:42
 */
val AppCompatActivity.launchError by lazy {
    MutableLiveData<Throwable>()
}

fun <T> AppCompatActivity.launch(
    context: CoroutineContext = Dispatchers.Main.immediate,
    tryBlock: suspend CoroutineScope.() -> T,
    catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
    finallyBlock: suspend CoroutineScope.() -> Unit = {}
): Job {
    return CoroutineScope(context).launch(context) {
        try {
            tryBlock()
        } catch (e: Throwable) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            catchBlock(e)
            launchError.postValue(e)
        } finally {
            finallyBlock()
        }
    }
}

fun <T> AppCompatActivity.launch(
    context: CoroutineContext = Dispatchers.Main.immediate,
    tryBlock: suspend CoroutineScope.() -> T
): Job {
    return launch(context, tryBlock, {}, {})
}

val Fragment.launchError by lazy {
    MutableLiveData<Throwable>()
}

fun <T> Fragment.launch(
    context: CoroutineContext = Dispatchers.Main.immediate,
    tryBlock: suspend CoroutineScope.() -> T,
    catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
    finallyBlock: suspend CoroutineScope.() -> Unit = {}
): Job {
    return CoroutineScope(context).launch(context) {
        try {
            tryBlock()
        } catch (e: Throwable) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            catchBlock(e)
            launchError.postValue(e)
        } finally {
            finallyBlock()
        }
    }
}

fun <T> Fragment.launch(
    context: CoroutineContext = Dispatchers.Main.immediate,
    tryBlock: suspend CoroutineScope.() -> T
): Job {
    return launch(context, tryBlock, {}, {})
}