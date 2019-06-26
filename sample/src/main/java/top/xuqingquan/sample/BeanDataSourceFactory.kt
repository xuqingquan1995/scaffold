package top.xuqingquan.sample

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class BeanDataSourceFactory : DataSource.Factory<Int, Subjects>() {

    val sourceLiveData = MutableLiveData<BeanDataSource>()

    override fun create(): DataSource<Int, Subjects> {
        val source = BeanDataSource()
        sourceLiveData.postValue(source)
        return source
    }
}