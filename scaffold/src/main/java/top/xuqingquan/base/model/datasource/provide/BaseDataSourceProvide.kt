package top.xuqingquan.base.model.datasource.provide

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import top.xuqingquan.app.AppComponentUtils
import top.xuqingquan.base.model.datasource.factory.BasePageKeyedDataSourceFactory
import top.xuqingquan.base.model.entity.Listing

/**
 * Created by 许清泉 on 2019-04-20 21:05
 */
class BaseDataSourceProvide(context: Context) {
    private val config = AppComponentUtils.obtainAppComponentFromContext(context).pageSize()
    @MainThread
    fun <Key, Value> getPageKeyedDataSource(factory: BasePageKeyedDataSourceFactory<Key, Value>): Listing<Value> {
        return Listing(
            pagedList = factory.toLiveData(config),
            networkState = Transformations.switchMap(factory.sourceLiveData) { it.networkState },
            refreshState = Transformations.switchMap(factory.sourceLiveData) { it.initialLoad },
            empty = Transformations.switchMap(factory.sourceLiveData) { it.empty },
            refresh = { factory.sourceLiveData.value?.invalidate() },
            retry = { factory.sourceLiveData.value?.retryAllFailed() },
            exception = Transformations.switchMap(factory.sourceLiveData) { it.exception }
        )
    }

    @MainThread
    fun <Key, Value> getItemKeyedDataSource(factory: BasePageKeyedDataSourceFactory<Key, Value>): Listing<Value> {
        return Listing(
            pagedList = factory.toLiveData(config),
            networkState = Transformations.switchMap(factory.sourceLiveData) { it.networkState },
            refreshState = Transformations.switchMap(factory.sourceLiveData) { it.initialLoad },
            empty = Transformations.switchMap(factory.sourceLiveData) { it.empty },
            refresh = { factory.sourceLiveData.value?.invalidate() },
            retry = { factory.sourceLiveData.value?.retryAllFailed() },
            exception = Transformations.switchMap(factory.sourceLiveData) { it.exception }
        )
    }
}