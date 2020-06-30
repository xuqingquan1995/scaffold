package top.xuqingquan.imageloader.glide

import android.graphics.Bitmap
import android.support.annotation.IntRange
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import top.xuqingquan.utils.doBlur
import java.security.MessageDigest

/**
 * Created by 许清泉 on 2019/4/16 01:02
 * 高斯模糊
 */
class BlurTransformation(@IntRange(from = 0) private val radius: Int = 15) :
    BitmapTransformation() {
    companion object {
        private val ID = this::class.java.name
        private val ID_BYTES = ID.toByteArray(Key.CHARSET)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) = messageDigest.update(ID_BYTES)

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return doBlur(toTransform, radius, true)
    }

    override fun equals(other: Any?) = other is BlurTransformation

    override fun hashCode() = ID.hashCode()
}