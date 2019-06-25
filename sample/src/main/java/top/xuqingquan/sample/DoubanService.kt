package top.xuqingquan.sample

import retrofit2.http.GET
import retrofit2.http.Query

interface DoubanService {

    @GET("/v2/movie/top250")
    suspend fun top250(@Query("apikey") apikey: String, @Query("start") start: Int, @Query("count") count: Int):Bean

}