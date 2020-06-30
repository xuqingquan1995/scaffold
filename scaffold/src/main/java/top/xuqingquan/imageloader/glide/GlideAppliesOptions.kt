package top.xuqingquan.imageloader.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry

/**
 * Created by 许清泉 on 2019-04-20 19:58
 */
interface GlideAppliesOptions {

    /**
     * 配置 @[Glide] 的自定义参数,此方法在 @[Glide] 初始化时执行(@[Glide] 在第一次被调用时初始化),只会执行一次
     *
     * @param context
     * @param builder [GlideBuilder] 此类被用来创建 Glide
     */
    fun applyGlideOptions(context: Context, builder: GlideBuilder)

    /**
     * 注册[Glide]的组件，参考[com.bumptech.glide.module.LibraryGlideModule]
     *
     * @param context  Android context
     * @param glide    [Glide]
     * @param registry [Registry]
     */
    fun registerComponents(context: Context, glide: Glide, registry: Registry)
}