package top.xuqingquan.delegate

import android.os.Bundle
import android.support.v4.app.Fragment
import top.xuqingquan.cache.Cache
import top.xuqingquan.cache.LruCache

/**
 * Created by 许清泉 on 2019/4/14 13:51
 */
interface IFragment {

    /**
     * 提供在 [Fragment] 生命周期内的缓存容器, 可向此 [Fragment] 存取一些必要的数据
     * 此缓存容器和 [Fragment] 的生命周期绑定, 如果 [Fragment] 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 [LifecycleModel](https://github.com/JessYanCoding/LifecycleModel)
     *
     * @return like [LruCache]
     */
    fun provideCache(): Cache<String, Any>

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 是否使用EventBus
     */
    fun useEventBus(): Boolean

    /**
     * 让持有该Fragment的Activity执行Fragment的方法
     */
    fun setData(data: Any? = null)
}