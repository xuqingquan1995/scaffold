package top.xuqingquan.imageloader

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.http.OkHttpUrlLoader
import top.xuqingquan.utils.FileUtils
import java.io.File
import java.io.InputStream

/**
 * Created by 许清泉 on 2019/4/16 00:13
 */
@GlideModule
class GlideConfiguration : AppGlideModule() {
    private val IMAGE_DISK_CACHE_MAX_SIZE: Long = 100 * 1024 * 1024//图片缓存文件最大值为100Mb

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache {
            DiskLruCacheWrapper.create(
                FileUtils.makeDirs(File(ScaffoldConfig.getCacheFile(), "Glide")),
                IMAGE_DISK_CACHE_MAX_SIZE
            )
        }
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize
        val customMemoryCacheSize = (1.2 * defaultMemoryCacheSize).toInt()
        val customBitmapPoolSize = (1.2 * defaultBitmapPoolSize).toInt()

        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize.toLong()))
        val loadImgStrategy = ScaffoldConfig.getImageLoader().loadImgStrategy
        if (loadImgStrategy is GlideAppliesOptions) {
            (loadImgStrategy as GlideAppliesOptions).applyGlideOptions(context, builder)
        }
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(ScaffoldConfig.getOkHttpClient())
        )
    }
}