package top.xuqingquan.http

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019/4/16 01:21
 * 展示 [GlobalHttpHandler] 的用法
 */
class GlobalHttpHandlerImpl:GlobalHttpHandler {

    override fun onHttpResultResponse(httpResult: String?, chain: Interceptor.Chain, response: Response): Response {
        Timber.v("onHttpResultResponse:$httpResult")
        return response
    }

    override fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request {
        Timber.v("onHttpRequestBefore:$request")
        return request
    }
}