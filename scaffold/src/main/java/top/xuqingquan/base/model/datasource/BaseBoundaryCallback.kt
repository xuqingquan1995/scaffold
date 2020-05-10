package top.xuqingquan.base.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.base.model.entity.NetworkStatus
import kotlin.coroutines.CoroutineContext

/**
 * Created by 许清泉 on 2020/5/10 14:09
 */
open class BaseBoundaryCallback<Value> : PagedList.BoundaryCallback<Value>() {
    /**
     * 用于更新全过程的网络状态
     */
    val networkState = MutableLiveData<NetworkStatus>()

    /**
     * 用于更新刚开始的网络状态
     */
    val initialLoad = MutableLiveData<NetworkStatus>()

    /**
     * 如果为空的话就用这个发射空数据提示
     */
    val empty = MutableLiveData<String>()

    /**
     * 重试的方法
     */
    protected var retry: (() -> Any)? = null

    /**
     * error监听
     */
    val exception = MutableLiveData<Throwable>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        launch {
            prevRetry?.invoke()
        }
    }

    protected fun <T> launch(
        context: CoroutineContext = Dispatchers.IO,
        tryBlock: suspend CoroutineScope.() -> T,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
        finallyBlock: suspend CoroutineScope.() -> Unit = {}
    ): Job {
        return CoroutineScope(context).launch {
            try {
                tryBlock()
            } catch (e: Throwable) {
                if (ScaffoldConfig.debug()) {
                    e.printStackTrace()
                }
                catchBlock(e)
                exception.postValue(e)
                initialLoad.postValue(NetworkStatus.FAILED)
                networkState.postValue(NetworkStatus.FAILED)
            } finally {
                finallyBlock()
            }
        }
    }

    protected fun <T> launch(
        context: CoroutineContext = Dispatchers.IO,
        tryBlock: suspend CoroutineScope.() -> T
    ): Job {
        return launch(context, tryBlock, {}, {})
    }
}