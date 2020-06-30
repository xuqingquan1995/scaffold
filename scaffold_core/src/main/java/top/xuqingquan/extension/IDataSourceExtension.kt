package top.xuqingquan.extension

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.base.model.datasource.IDataSource
import top.xuqingquan.base.model.entity.NetworkStatus
import kotlin.coroutines.CoroutineContext

/**
 * Create by 许清泉 on 2020/6/9 19:10
 */

/**
 * 获取Retrofit的Service
 */
val IDataSource.repositoryManager by lazy {
    ScaffoldConfig.getRepositoryManager()
}

/**
 * 用于更新全过程的网络状态
 */
val IDataSource.networkState by lazy {
    MutableLiveData<NetworkStatus>()
}

/**
 * 用于更新刚开始的网络状态
 */
val IDataSource.initialLoad by lazy {
    MutableLiveData<NetworkStatus>()
}

/**
 * 如果为空的话就用这个发射空数据提示
 */
val IDataSource.empty by lazy {
    MutableLiveData<String>()
}

var retry: (() -> Any)? = null

/**
 * error监听
 */
val IDataSource.exception by lazy {
    MutableLiveData<Throwable>()
}

fun IDataSource.retryAllFailed() {
    val prevRetry = retry
    retry = null
    prevRetry?.invoke()
}

fun <T> IDataSource.launch(
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

fun <T> IDataSource.launch(
    context: CoroutineContext = Dispatchers.IO,
    tryBlock: suspend CoroutineScope.() -> T
): Job {
    return launch(context, tryBlock, {}, {})
}