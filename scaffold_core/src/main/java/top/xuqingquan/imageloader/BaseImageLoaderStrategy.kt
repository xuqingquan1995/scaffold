package top.xuqingquan.imageloader

import android.content.Context

/**
 * Created by 许清泉 on 2019/4/14 20:31
 */
interface BaseImageLoaderStrategy<T : ImageConfig> {

    /**
     * 加载图片
     *
     * @param ctx [Context]
     * @param config 图片加载配置信息
     */
    fun loadImage(ctx: Context, config: T)

    /**
     * 停止加载
     *
     * @param ctx [Context]
     * @param config 图片加载配置信息
     */
    fun clear(ctx: Context?, config: T)
}