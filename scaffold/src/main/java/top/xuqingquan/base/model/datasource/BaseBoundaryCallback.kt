package top.xuqingquan.base.model.datasource

import androidx.paging.PagedList

/**
 * Created by 许清泉 on 2020/5/10 14:09
 */
open class BaseBoundaryCallback<Value> : PagedList.BoundaryCallback<Value>(), IDataSource