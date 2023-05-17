package top.xuqingquan.integration

import retrofit2.Retrofit

/**
 * Created by 许清泉 on 2019/4/14 16:42
 * 用来管理网络请求层,以及数据缓存层
 */
interface IRepositoryManager {

    /**
     * 根据传入的 name 和 Class 获取对应的 Retrofit service
     *
     * @param name 要获取的retrofit名称
     * @param service Retrofit service class
     * @param T     Retrofit service 类型
     * @return Retrofit service
     */
    fun <T> obtainRetrofitService(name: String, service: Class<T>): T

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service Retrofit service class
     * @param T     Retrofit service 类型
     * @return Retrofit service
     */
    fun <T> obtainRetrofitService(service: Class<T>): T

    /**
     * 刷新retrofit缓存
     */
    fun refreshRetrofitService()

    interface ObtainServiceDelegate {
        fun <T> createRetrofitService(retrofit: Retrofit?, serviceClass: Class<T>?): T?
    }

}