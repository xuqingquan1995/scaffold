package top.xuqingquan.web.agent

interface IUrlLoader {

    val httpHeaders: HttpHeaders

    fun loadUrl(url: String)

    fun loadUrl(url: String, headers: Map<String, String>)

    fun reload()

    fun loadData(data: String, mimeType: String, encoding: String)

    fun stopLoading()

    fun loadDataWithBaseURL(
        baseUrl: String, data: String,
        mimeType: String, encoding: String, historyUrl: String
    )

    fun postUrl(url: String, params: ByteArray)
}
