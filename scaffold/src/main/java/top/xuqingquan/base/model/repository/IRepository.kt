package top.xuqingquan.base.model.repository

/**
 * Created by 许清泉 on 2019-04-20 22:31
 */
interface IRepository {
    /**
     * 释放资源处理
     */
    fun onDestroy()
}