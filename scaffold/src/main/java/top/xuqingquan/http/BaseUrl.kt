package top.xuqingquan.http

import okhttp3.HttpUrl

/**
 * Created by 许清泉 on 2019/4/14 20:18
 */
interface BaseUrl {

    fun url():HttpUrl?
}