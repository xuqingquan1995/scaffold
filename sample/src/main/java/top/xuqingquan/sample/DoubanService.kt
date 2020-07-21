package top.xuqingquan.sample

import retrofit2.http.GET
import retrofit2.http.Query

interface DoubanService {

    @GET("/v2/movie/top250")
    suspend fun top250(
        @Query("apikey") apikey: String,
        @Query("start") start: Int,
        @Query("count") count: Int
    ): Bean

    @GET("/v2/movie/top250")
    fun top250V2(
        @Query("apikey") apikey: String,
        @Query("start") start: Int,
        @Query("count") count: Int
    ): Bean

    //http://so.techlz.com:9972/main/api/removeAd7/?co=377&sign=CDEC8D&tta=159507&os=1
    @GET("/main/api/removeAd7/")
    suspend fun removeAd(
        @Query("co") co: Int,
        @Query("sign") sign: String,
        @Query("tta") tta: Int,
        @Query("os") os: Int
    ): ApiResult<List<Map<String, String>>>

    @GET("main/api/siteKeywords7/")
    suspend fun siteKeywords(@Query("g") group: String): ApiResult<MutableList<String>>

}