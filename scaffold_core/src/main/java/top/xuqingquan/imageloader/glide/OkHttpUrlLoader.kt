package top.xuqingquan.imageloader.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * Created by 许清泉 on 2019/4/16 00:35
 */
class OkHttpUrlLoader private constructor(private val client: Call.Factory) : ModelLoader<GlideUrl, InputStream> {

    override fun buildLoadData(
        model: GlideUrl,
        width: Int,
        height: Int,
        options: Options
    ) = ModelLoader.LoadData(model, OkHttpStreamFetcher(client, model))

    override fun handles(model: GlideUrl) = true

    internal class Factory(private val client: Call.Factory = getInternalClient()) : ModelLoaderFactory<GlideUrl, InputStream> {

        companion object {
            @Volatile
            private var internalClient: Call.Factory? = null

            private fun getInternalClient(): Call.Factory {
                if (internalClient == null) {
                    synchronized(Factory::class.java) {
                        if (internalClient == null) {
                            internalClient = OkHttpClient()
                        }
                    }
                }
                return internalClient!!
            }
        }

        override fun build(multiFactory: MultiModelLoaderFactory) = OkHttpUrlLoader(client)

        override fun teardown() {
        }
    }
}