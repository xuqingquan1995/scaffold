package top.xuqingquan.base.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import kotlinx.coroutines.*
import top.xuqingquan.BuildConfig
import top.xuqingquan.base.model.entity.NetworkStatus
import kotlin.coroutines.CoroutineContext

/**
 * Created by 许清泉 on 2019-04-20 18:35
 */
abstract class BaseItemKeyedDataSource<Key, Value> : ItemKeyedDataSource<Key, Value>() {

    /**
     * 用于更新全过程的网络状态
     */
    protected val networkState = MutableLiveData<NetworkStatus>()
    /**
     * 用于更新刚开始的网络状态
     */
    protected val initialLoad = MutableLiveData<NetworkStatus>()
    /**
     * 如果为空的话就用这个发射空数据提示
     */
    protected val empty = MutableLiveData<String>()
    /**
     * 重试的方法
     */
    private var retry: (() -> Any)? = null

    /**
     * error监听
     */
    val exception = MutableLiveData<Throwable>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        runBlocking {
            launch(Dispatchers.IO){
                prevRetry?.invoke()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Value>) {
    }

    protected fun <T> launch(
        context: CoroutineContext = Dispatchers.Default,
        tryBlock: suspend CoroutineScope.() -> T,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
        finallyBlock: suspend CoroutineScope.() -> Unit = {}
    ): Job {
        return CoroutineScope(context).launch {
            tryCatch(tryBlock, catchBlock, finallyBlock)
        }
    }

    protected fun <T> launch(
        context: CoroutineContext = Dispatchers.Default,
        tryBlock: suspend CoroutineScope.() -> T
    ): Job {
        return launch(context, tryBlock, {}, {})
    }

    private suspend fun <T> tryCatch(
        tryBlock: suspend CoroutineScope.() -> T,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
                catchBlock(e)
                exception.postValue(e)
            } finally {
                finallyBlock()
            }
        }
    }

}