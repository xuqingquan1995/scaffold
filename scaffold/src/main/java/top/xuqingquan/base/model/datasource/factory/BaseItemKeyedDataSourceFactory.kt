package top.xuqingquan.base.model.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import top.xuqingquan.base.model.datasource.BaseItemKeyedDataSource

/**
 * Created by 许清泉 on 2019-04-20 20:53
 */
open class BaseItemKeyedDataSourceFactory<Key, Value>(private val dataSource: BaseItemKeyedDataSource<Key, Value>) :
    DataSource.Factory<Key, Value>() {

    val sourceLiveData = MutableLiveData<BaseItemKeyedDataSource<Key, Value>>()

    override fun create(): DataSource<Key, Value> {
        sourceLiveData.postValue(dataSource)
        return dataSource
    }
}