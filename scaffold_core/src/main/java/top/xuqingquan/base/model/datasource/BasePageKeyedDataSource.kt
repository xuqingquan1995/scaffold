package top.xuqingquan.base.model.datasource

import androidx.paging.PageKeyedDataSource

/**
 * Created by 许清泉 on 2019-04-20 18:44
 */
abstract class BasePageKeyedDataSource<Key, Value> : PageKeyedDataSource<Key, Value>(),
    IDataSource {

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
    }

}