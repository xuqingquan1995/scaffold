package top.xuqingquan.imageloader

import android.content.Context
import androidx.core.util.Preconditions
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019/4/16 01:29
 */
class GlideImageLoaderStrategy : BaseImageLoaderStrategy<ImageConfigImpl>, GlideAppliesOptions {

    override fun loadImage(ctx: Context?, config: ImageConfigImpl?) {
        Preconditions.checkNotNull(ctx, "Context is required")
        Preconditions.checkNotNull(config, "ImageConfigImpl is required")
        Preconditions.checkNotNull(config!!.imageView, "ImageView is required")
        val requests: GlideRequests = GlideApp.with(ctx!!)
        //如果context是activity则自动使用Activity的生命周期
        val glideRequest = requests.load(config.getUrl())
        when (config.cacheStrategy) {
            //缓存策略
            0 -> glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
            1 -> glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE)
            2 -> glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            3 -> glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA)
            4 -> glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
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
        if (config.getPlaceholder() !== 0) {
            //设置占位符
            glideRequest.placeholder(config.getPlaceholder())
        }
        if (config.getErrorPic() !== 0) {
            //设置错误的图片
            glideRequest.error(config.getErrorPic())
        }
        if (config.fallback !== 0) {
            //设置请求 url 为空图片
            glideRequest.fallback(config.fallback)
        }
        glideRequest.into(config.getImageView())
    }

    override fun clear(ctx: Context?, config: ImageConfigImpl?) {
        Preconditions.checkNotNull(ctx, "Context is required")
        Preconditions.checkNotNull(config, "ImageConfigImpl is required")
        if (config!!.getImageView() != null) {
            GlideApp.get(ctx!!).requestManagerRetriever.get(ctx).clear(config.getImageView())
        }

        if (config.imageViews != null && config.imageViews.isNotEmpty()) {//取消在执行的任务并且释放资源
            for (imageView in config.imageViews) {
                GlideApp.get(ctx!!).requestManagerRetriever.get(ctx).clear(imageView)
            }
        }

        if (config.isClearDiskCache) {//清除本地缓存
            runBlocking {
                launch(Dispatchers.IO) {
                    Glide.get(ctx!!).clearDiskCache()
                }
            }
        }

        if (config.isClearMemory) {//清除内存缓存
            runBlocking {
                launch(Dispatchers.IO) {
                    Glide.get(ctx!!).clearMemory()
                }
            }
        }
    }

    override fun applyGlideOptions(context: Context, builder: GlideBuilder) {
        Timber.d("applyGlideOptions")
    }
}