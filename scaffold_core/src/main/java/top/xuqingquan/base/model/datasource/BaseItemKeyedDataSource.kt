package top.xuqingquan.base.model.datasource

import androidx.paging.ItemKeyedDataSource

/**
 * Created by 许清泉 on 2019-04-20 18:35
 */
abstract class BaseItemKeyedDataSource<Key, Value> : ItemKeyedDataSource<Key, Value>(),
    IDataSource {

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Value>) {
    }
}