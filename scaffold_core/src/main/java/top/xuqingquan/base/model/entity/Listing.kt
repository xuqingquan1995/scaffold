package top.xuqingquan.base.model.entity

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

/**
 * Created by 许清泉 on 2019-04-20 20:44
 * 数据更新作用
 */
data class Listing<T>(
    val empty: LiveData<String>,
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkStatus>,
    val refreshState: LiveData<NetworkStatus>,
    val refresh: () -> Unit,
    val retry: () -> Unit,
    val exception: LiveData<Throwable>
)