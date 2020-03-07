package top.xuqingquan.sample

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource

class BeanDataSourceFactory : DataSource.Factory<Int, Subjects>() {

    val sourceLiveData = MutableLiveData<BeanDataSource>()

    override fun create(): DataSource<Int, Subjects> {
        val source = BeanDataSource()
        sourceLiveData.postValue(source)
        return source
    }
}