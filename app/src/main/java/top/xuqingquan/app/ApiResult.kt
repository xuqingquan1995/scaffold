package top.xuqingquan.app

/**
 *  @author : 许清泉 xuqingquan1995@gmail.com
 *  @since   : 2021-10-15
 */
data class ApiResult<T>(var code: Int, var msg: String?, var data: T?)