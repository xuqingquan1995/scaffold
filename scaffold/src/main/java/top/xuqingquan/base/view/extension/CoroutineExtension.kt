package top.xuqingquan.base.view.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
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
    context: CoroutineContext = lifecycleScope.coroutineContext,
    tryBlock: suspend CoroutineScope.() -> T,
    catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
    finallyBlock: suspend CoroutineScope.() -> Unit = {}
): Job {
    return lifecycleScope.launch(context) {
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
    context: CoroutineContext = lifecycleScope.coroutineContext,
    tryBlock: suspend CoroutineScope.() -> T
): Job {
    return launch(context, tryBlock, {}, {})
}

val Fragment.launchError by lazy {
    MutableLiveData<Throwable>()
}

fun <T> Fragment.launch(
    context: CoroutineContext = lifecycleScope.coroutineContext,
    tryBlock: suspend CoroutineScope.() -> T,
    catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
    finallyBlock: suspend CoroutineScope.() -> Unit = {}
): Job {
    return lifecycleScope.launch(context) {
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
    context: CoroutineContext = lifecycleScope.coroutineContext,
    tryBlock: suspend CoroutineScope.() -> T
): Job {
    return launch(context, tryBlock, {}, {})
}