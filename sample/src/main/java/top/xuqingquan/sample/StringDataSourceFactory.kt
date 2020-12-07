package top.xuqingquan.sample

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class StringDataSourceFactory : DataSource.Factory<Int, String>() {

    val sourceLiveData = MutableLiveData<StringDataSource>()

    override fun create(): DataSource<Int, String> {
        val source = StringDataSource()
        sourceLiveData.postValue(source)
        return source
    }
}