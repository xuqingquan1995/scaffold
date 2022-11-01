package top.xuqingquan.imageloader.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.util.ContentLengthInputStream
import com.bumptech.glide.util.Preconditions
import okhttp3.*
import top.xuqingquan.utils.Timber
import java.io.IOException
import java.io.InputStream

/**
 * Created by 许清泉 on 2019/4/16 00:26
 */
internal class OkHttpStreamFetcher(private val client: Call.Factory, private val url: GlideUrl) :
    DataFetcher<InputStream>,
    Callback {

    private var stream: InputStream? = null
    private var responseBody: ResponseBody? = null
    private var callback: DataFetcher.DataCallback<in InputStream>? = null

    @Volatile
    private var call: Call? = null

    override fun getDataClass() = InputStream::class.java

    override fun cleanup() {
        try {
            stream?.close()
        } catch (_: IOException) {
        }
        responseBody?.apply { close() }
        callback = null
    }

    override fun getDataSource() = DataSource.REMOTE

    override fun cancel() {
        call?.cancel()
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val requestBuilder = Request.Builder().url(url.toStringUrl())
        for ((key, value) in url.headers) {
            requestBuilder.addHeader(key, value)
        }
        val request = requestBuilder.build()
        this.callback = callback
        call = client.newCall(request)
        call?.enqueue(this)
    }

    override fun onFailure(call: Call, e: IOException) {
        Timber.e(e, "OkHttp failed to obtain result")
        callback?.onLoadFailed(e)
    }

    override fun onResponse(call: Call, response: Response) {
        responseBody = response.body
        if (response.isSuccessful) {
            val contentLength = Preconditions.checkNotNull(responseBody).contentLength()
            stream = ContentLengthInputStream.obtain(responseBody!!.byteStream(), contentLength)
            callback?.onDataReady(stream)
        } else {
            callback?.onLoadFailed(HttpException(response.message, response.code))
        }
    }
}