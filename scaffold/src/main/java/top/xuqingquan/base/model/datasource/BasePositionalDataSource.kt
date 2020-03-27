package top.xuqingquan.base.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.base.model.entity.NetworkStatus
import kotlin.coroutines.CoroutineContext

abstract class BasePositionalDataSource<Value> : PositionalDataSource<Value>() {

    protected val repositoryManager = ScaffoldConfig.getRepositoryManager()
    /**
     * 用于全过程的加载状态
     */
    val LoadState = MutableLiveData<NetworkStatus>()
    /**
     * 用于刚开始的加载状态
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
                LoadState.postValue(NetworkStatus.FAILED)
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