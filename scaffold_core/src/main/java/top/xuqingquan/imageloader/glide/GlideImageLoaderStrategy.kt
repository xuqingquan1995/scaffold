package top.xuqingquan.imageloader.glide

import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.xuqingquan.imageloader.*
import top.xuqingquan.utils.Preconditions
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019/4/16 01:29
 */
class GlideImageLoaderStrategy : BaseImageLoaderStrategy<ImageConfigImpl>, GlideAppliesOptions {

    @SuppressLint("CheckResult")
    override fun loadImage(ctx: Context, config: ImageConfigImpl) {
        Preconditions.checkNotNull(ctx, "Context is required")
        Preconditions.checkNotNull(config, "ImageConfigImpl is required")
        Preconditions.checkNotNull(config.imageView, "ImageView is required")
        val requests: GlideRequests = GlideApp.with(ctx)
        //如果context是activity则自动使用Activity的生命周期
        val glideRequest = requests.load(config.url)
        when (config.cacheStrategy) {
            //缓存策略
            CacheStrategy.NONE -> glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE)
            CacheStrategy.RESOURCE -> glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            CacheStrategy.DATA -> glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA)
            CacheStrategy.AUTOMATIC -> glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            else -> glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        if (config.isCrossFade) {
            glideRequest.transition(DrawableTransitionOptions.withCrossFade())
        }
        if (config.isCenterCrop) {
            glideRequest.centerCrop()
        }
        if (config.isCircle) {
            glideRequest.circleCrop()
        }
        if (config.isImageRadius) {
            glideRequest.transform(RoundedCorners(config.imageRadius))
        }
        if (config.isBlurImage) {
            glideRequest.transform(BlurTransformation(config.blurValue))
        }
        if (config.transformation != null) {//glide用它来改变图形的形状
            glideRequest.transform(config.transformation)
        }
        if (config.placeholder != 0) {
            //设置占位符
            glideRequest.placeholder(config.placeholder)
        }
        if (config.errorPic != 0) {
            //设置错误的图片
            glideRequest.error(config.errorPic)
        }
        if (config.fallback != 0) {
            //设置请求 url 为空图片
            glideRequest.fallback(config.fallback)
        }
        glideRequest.into(config.imageView)
    }

    override fun clear(ctx: Context?, config: ImageConfigImpl) {
        Preconditions.checkNotNull(ctx, "Context is required")
        Preconditions.checkNotNull(config, "ImageConfigImpl is required")
        if (config.imageView != null) {
            GlideApp.get(ctx!!).requestManagerRetriever.get(ctx).clear(config.imageView)
        }

        if (config.imageViews != null && config.imageViews.isNotEmpty()) {//取消在执行的任务并且释放资源
            for (imageView in config.imageViews) {
                GlideApp.get(ctx!!).requestManagerRetriever.get(ctx).clear(imageView)
            }
        }

        if (config.isClearDiskCache) {//清除本地缓存
            CoroutineScope(Dispatchers.IO).launch {
                Glide.get(ctx!!).clearDiskCache()
            }
        }

        if (config.isClearMemory) {//清除内存缓存
            CoroutineScope(Dispatchers.IO).launch {
                Glide.get(ctx!!).clearMemory()
            }
        }
    }

    override fun applyGlideOptions(context: Context, builder: GlideBuilder) {
        Timber.d("applyGlideOptions")
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        Timber.i("registerComponents")
    }
}