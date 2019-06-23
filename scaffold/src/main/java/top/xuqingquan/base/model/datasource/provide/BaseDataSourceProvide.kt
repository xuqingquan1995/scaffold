package top.xuqingquan.base.model.datasource.provide

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.toLiveData
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.base.model.datasource.BaseItemKeyedDataSource
import top.xuqingquan.base.model.datasource.BasePageKeyedDataSource
import top.xuqingquan.base.model.datasource.factory.BaseItemKeyedDataSourceFactory
import top.xuqingquan.base.model.datasource.factory.BasePageKeyedDataSourceFactory
import top.xuqingquan.base.model.entity.Listing

/**
 * Created by 许清泉 on 2019-04-20 21:05
 */
class BaseDataSourceProvide(context: Context) {

    private val config = ScaffoldConfig.getPagedListConfig()

    @MainThread
    fun <Key, Value> getDataSource(factory: BasePageKeyedDataSourceFactory<Key, Value>): Listing<Value> {
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
    fun <Key, Value> getDataSource(factory: BaseItemKeyedDataSourceFactory<Key, Value>): Listing<Value> {
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

    /**
     * 类型转换失败时候可能为空
     */
    private fun <Key, Value> getDataSourceFactory(dataSource: DataSource<Key, Value>): DataSource.Factory<Key, Value>? {
        if (dataSource !is BaseItemKeyedDataSource && dataSource !is BasePageKeyedDataSource) {
            throw ClassCastException("dataSource 类型转换失败")
        }
        return when (dataSource) {
            is BaseItemKeyedDataSource -> BaseItemKeyedDataSourceFactory(dataSource)
            is BasePageKeyedDataSource -> BasePageKeyedDataSourceFactory(dataSource)
            else -> null
        }
    }

    /**
     * 类型转换失败时候可能为空
     */
    fun <Key, Value> getListing(dataSource: DataSource<Key, Value>): Listing<Value>? {
        return when (val factory = getDataSourceFactory(dataSource)) {
            is BaseItemKeyedDataSourceFactory -> getDataSource(factory)
            is BasePageKeyedDataSourceFactory -> getDataSource(factory)
            else -> null
        }
    }
}