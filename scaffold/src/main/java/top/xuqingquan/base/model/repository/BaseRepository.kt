package top.xuqingquan.base.model.repository

import android.content.Context
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.integration.IRepositoryManager

/**
 * Created by 许清泉 on 2019-04-20 22:32
 */
open class BaseRepository : IRepository {
    protected var context: Context? = ScaffoldConfig.getApplication()
    protected var mRepositoryManager: IRepositoryManager? = ScaffoldConfig.getRepositoryManager()

    override fun onDestroy() {
        mRepositoryManager = null
        context = null
    }
}