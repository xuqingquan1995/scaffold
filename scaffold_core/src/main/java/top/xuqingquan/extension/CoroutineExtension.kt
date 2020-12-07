package top.xuqingquan.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.base.view.activity.SimpleActivity
import top.xuqingquan.base.view.fragment.SimpleFragment
import kotlin.coroutines.CoroutineContext

/**
 * Create by 许清泉 on 2020/6/9 18:42
 */

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
            if (ScaffoldConfig.debug()) {
                e.printStackTrace()
            }
            catchBlock(e)
            if (this@launch is SimpleActivity) {
                launchError.postValue(e)
            }
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
            if (ScaffoldConfig.debug()) {
                e.printStackTrace()
            }
            catchBlock(e)
            if (this@launch is SimpleFragment) {
                launchError.postValue(e)
            }
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