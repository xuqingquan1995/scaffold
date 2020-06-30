package top.xuqingquan.http.log

import okhttp3.Interceptor
import okhttp3.Response
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.utils.Timber.Forest.w
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by 许清泉 on 2019/4/14 20:51
 */
class RequestInterceptor private constructor() : Interceptor {
    private val mPrinter = ScaffoldConfig.getFormatPrinter()
    private val mHandler = ScaffoldConfig.getGlobalHttpHandler()
    private val printLevel = ScaffoldConfig.getLevel()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val logRequest = (printLevel === Level.ALL || printLevel === Level.REQUEST)
        if (logRequest) {
            //打印请求信息
            if (request.body() != null && isParseable(request.body()!!.contentType())) {
                mPrinter.printJsonRequest(request, parseParams(request))
            } else {
                mPrinter.printFileRequest(request)
            }
        }
        val logResponse = (printLevel === Level.ALL || printLevel === Level.RESPONSE)
        val t1 = if (logResponse) System.nanoTime() else 0
        val originalResponse = try {
            chain.proceed(request)
        } catch (e: Throwable) {
            w("Http Error: $e")
            throw e
        }
        val t2 = if (logResponse) {
            System.nanoTime()
        } else {
            0
        }
        val responseBody = originalResponse.body()
        //打印响应结果
        var bodyString: String? = null
        if (responseBody != null && isParseable(responseBody.contentType())) {
            bodyString = printResult(originalResponse)
        }
        if (logResponse) {
            val segmentList = request.url().encodedPathSegments()
            val header = originalResponse.headers().toString()
            val code = originalResponse.code()
            val isSuccessful = originalResponse.isSuccessful
            val message = originalResponse.message()
            val url = originalResponse.request().url().toString()
            if (responseBody != null && isParseable(responseBody.contentType())) {
                mPrinter.printJsonResponse(
                    TimeUnit.NANOSECONDS.toMillis(t2 - t1), isSuccessful,
                    code, header, responseBody.contentType(), bodyString, segmentList, message, url
                )
            } else {
                mPrinter.printFileResponse(
                    TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                    isSuccessful, code, header, segmentList, message, url
                )
            }
        }
        //这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
        return mHandler.onHttpResultResponse(bodyString, chain, originalResponse)
    }

    companion object {
        private var instance: RequestInterceptor? = null

        @JvmStatic
        fun getInstance(): RequestInterceptor {
            if (instance == null) {
                synchronized(RequestInterceptor::class.java) {
                    if (instance == null) {
                        instance = RequestInterceptor()
                    }
                }
            }
            return instance!!
        }
    }
}