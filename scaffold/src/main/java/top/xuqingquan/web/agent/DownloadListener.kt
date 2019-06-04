package top.xuqingquan.web.agent

/**
 * Created by 许清泉 on 2019-06-05 00:56
 */
open class DownloadListener {

    open fun onProgress(url: String, downloaded: Long, length: Long, usedTime: Long) {}

}