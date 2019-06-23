package top.xuqingquan.base.model.repository

import android.content.Context
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.integration.IRepositoryManager

/**
 * Created by 许清泉 on 2019-04-20 22:32
 */
open class BaseRepository : IRepository {
    protected var context: Context?
    protected var mRepositoryManager: IRepositoryManager?

    init {
        mRepositoryManager = ScaffoldConfig.getRepositoryManager();
        context = ScaffoldConfig.getApplication()
    }

    override fun onDestroy() {
        mRepositoryManager = null
        context = null
    }
}