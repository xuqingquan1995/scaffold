package top.xuqingquan.base.model.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import top.xuqingquan.base.model.datasource.BasePageKeyedDataSource

/**
 * Created by 许清泉 on 2019-04-20 20:53
 */
open class BasePageKeyedDataSourceFactory<Key, Value>(private val dataSource: BasePageKeyedDataSource<Key, Value>) :
    DataSource.Factory<Key, Value>() {

    val sourceLiveData = MutableLiveData<BasePageKeyedDataSource<Key, Value>>()

    override fun create(): DataSource<Key, Value> {
        sourceLiveData.postValue(dataSource)
        return dataSource
    }
}