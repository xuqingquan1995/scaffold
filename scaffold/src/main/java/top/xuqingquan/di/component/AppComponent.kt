package top.xuqingquan.di.component

import android.app.Application
import androidx.paging.PagedList
import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import okhttp3.OkHttpClient
import top.xuqingquan.cache.Cache
import top.xuqingquan.delegate.AppDelegate
import top.xuqingquan.di.module.AppModule
import top.xuqingquan.di.module.ClientModule
import top.xuqingquan.di.module.GlobalConfigModule
import top.xuqingquan.imageloader.ImageLoader
import top.xuqingquan.integration.IRepositoryManager
import java.io.File
import java.util.concurrent.ExecutorService
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by 许清泉 on 2019/4/14 14:16
 */
@Singleton
@Component(modules = [AppModule::class, ClientModule::class, GlobalConfigModule::class])
interface AppComponent {

    fun application(): Application
    /**
     * 用于管理网络请求层, 以及数据缓存层
     *
     * @return [IRepositoryManager]
     */
    fun repositoryManager(): IRepositoryManager

    /**
     * 图片加载管理器, 用于加载图片的管理类, 使用策略者模式, 可在运行时动态替换任何图片加载框架
     * arms-imageloader-glide 提供 Glide 的策略实现类, 也可以自行实现
     * 需要在 [ConfigModule.applyOptions] 中
     * 手动注册 [BaseImageLoaderStrategy], [ImageLoader] 才能正常使用
     *
     * @return
     */
    fun imageLoader(): ImageLoader

    /**
     * 网络请求框架
     *
     * @return [OkHttpClient]
     */
    fun okHttpClient(): OkHttpClient

    /**
     * Json 序列化库
     *
     * @return [Gson]
     */
    fun gson(): Gson

    /**
     * 缓存文件根目录 (RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下), 应该将所有缓存都统一放到这个根目录下
     * 便于管理和清理, 可在 [ConfigModule.applyOptions] 种配置
     *
     * @return [File]
     */
    fun cacheFile(): File

    /**
     * 用来存取一些整个 App 公用的数据, 切勿大量存放大容量数据, 这里的存放的数据和 [Application] 的生命周期一致
     *
     * @return [Cache]
     */
    fun extras(): Cache<String, Any>

    /**
     * 用于创建框架所需缓存对象的工厂
     *
     * @return [Cache.Factory]
     */
    fun cacheFactory(): Cache.Factory

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return [ExecutorService]
     */
    fun executorService(): ExecutorService

    /**
     * 返回分页的Config
     */
    fun pageSize(): PagedList.Config

    @Named("showStack")
    fun showStack():Boolean

    fun inject(delegate: AppDelegate)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun globalConfigModule(globalConfigModule: GlobalConfigModule): Builder

        fun build(): AppComponent
    }
}