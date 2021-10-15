package top.xuqingquan.app

import retrofit2.http.GET
import retrofit2.http.Headers

/**
 *  @author : 许清泉 xuqingquan1995@gmail.com
 *  @since   : 2021-10-15
 */
interface HttpsService2 {

    @Headers("Domain-Name: URL_2")
    @GET("admin/app")
    suspend fun getAppList2(): ApiResult<List<Any>>
}