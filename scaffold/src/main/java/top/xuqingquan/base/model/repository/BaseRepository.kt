package top.xuqingquan.base.model.repository

import android.content.Context
import top.xuqingquan.integration.IRepositoryManager
import javax.inject.Inject

/**
 * Created by 许清泉 on 2019-04-20 22:32
 */
open class BaseRepository @Inject constructor(
    private var context: Context?,
    private var mRepositoryManager: IRepositoryManager?
) : IRepository {

    override fun onDestroy() {
        mRepositoryManager = null
        context = null
    }
}