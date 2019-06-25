package top.xuqingquan.sample

import top.xuqingquan.base.model.datasource.BasePageKeyedDataSource
import top.xuqingquan.base.model.entity.NetworkStatus
import top.xuqingquan.utils.Timber

class BeanDataSource : BasePageKeyedDataSource<Int, Subjects>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Subjects>) {
        Timber.d("loadInitial-----params.requestedLoadSize===${params.requestedLoadSize}")
        networkState.postValue(NetworkStatus.RUNNING)
        initialLoad.postValue(NetworkStatus.RUNNING)
        launch {
            val bean = repositoryManager.obtainRetrofitService(DoubanService::class.java)
                .top250("0b2bdeda43b5688921839c8ecb20399b", 0, params.requestedLoadSize)
            callback.onResult(bean.subjects, 0, bean.total, 0, 1)
            networkState.postValue(NetworkStatus.SUCCESS)
            initialLoad.postValue(NetworkStatus.SUCCESS)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Subjects>) {
        Timber.d("loadAfter-----params.key===${params.key}")
        Timber.d("loadAfter-----params.requestedLoadSize===${params.requestedLoadSize}")
        networkState.postValue(NetworkStatus.RUNNING)
        launch {
            val bean = repositoryManager.obtainRetrofitService(DoubanService::class.java)
                .top250(
                    "0b2bdeda43b5688921839c8ecb20399b",
                    params.key * params.requestedLoadSize,
                    params.requestedLoadSize
                )
            callback.onResult(bean.subjects, params.key + 1)
        }
    }
}