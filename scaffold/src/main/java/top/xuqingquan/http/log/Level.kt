package top.xuqingquan.http.log

/**
 * Created by 许清泉 on 2019/4/14 21:08
 */
enum class Level {
    /**
     * 不打印log
     */
    NONE,
    /**
     * 只打印请求信息
     */
    REQUEST,
    /**
     * 只打印响应信息
     */
    RESPONSE,
    /**
     * 所有数据全部打印
     */
    ALL
}