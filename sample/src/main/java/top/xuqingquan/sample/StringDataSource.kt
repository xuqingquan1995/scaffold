package top.xuqingquan.sample

import top.xuqingquan.base.model.datasource.BasePageKeyedDataSource
import top.xuqingquan.base.model.entity.NetworkStatus
import top.xuqingquan.extension.*

/**
 * Created by 许清泉 on 12/7/20 10:04 AM
 */
class StringDataSource: BasePageKeyedDataSource<Int, String>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, String>
    ) {
        initialLoad.postValue(NetworkStatus.RUNNING)
        launch(tryBlock = {
            val data=repositoryManager.obtainRetrofitService(DoubanService::class.java)
                .siteKeywords("1256")
            callback.onResult(data.data?: emptyList(),0,1)
            initialLoad.postValue(NetworkStatus.SUCCESS)
        },catchBlock = {
            retry = {
                loadInitial(params, callback)
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, String>) {
        networkState.postValue(NetworkStatus.RUNNING)
        launch(tryBlock = {
            val data=repositoryManager.obtainRetrofitService(DoubanService::class.java)
                .siteKeywords("1256")
            callback.onResult(data.data?: emptyList(),params.key + 1)
            networkState.postValue(NetworkStatus.SUCCESS)
        },catchBlock = {
            retry = {
                loadAfter(params, callback)
            }
        })
    }
}