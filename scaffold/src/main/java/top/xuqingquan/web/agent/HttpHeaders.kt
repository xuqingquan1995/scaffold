package top.xuqingquan.web.agent

import android.text.TextUtils
import androidx.collection.ArrayMap

class HttpHeaders internal constructor() {

    val headers = ArrayMap<String, MutableMap<String, String>>()

    fun getHeaders(url: String?): MutableMap<String, String>? {
        val subUrl = subBaseUrl(url)
        if (headers[subUrl] == null) {
            val headers = ArrayMap<String, String>()
            this.headers[subUrl!!] = headers
            return headers
        }
        return headers[subUrl]
    }

    fun additionalHttpHeader(o_url: String?, k: String, v: String) {
        var url: String? = o_url ?: return
        url = subBaseUrl(url)
        val mHeaders = this.headers
        var headersMap: MutableMap<String, String>? = mHeaders[subBaseUrl(url)]
        if (null == headersMap) {
            headersMap = ArrayMap()
        }
        headersMap[k] = v
        mHeaders[url!!] = headersMap
    }


    fun additionalHttpHeaders(url: String?, headers: MutableMap<String, String>) {
        url ?: return
        val subUrl = subBaseUrl(url)
        val mHeaders = this.headers
        var headersMap: MutableMap<String, String>? = headers
        if (null == headersMap) {
            headersMap = ArrayMap()
        }
        mHeaders[subUrl!!] = headersMap
    }

    fun removeHttpHeader(url: String?, k: String) {
        url ?: return
        val subUrl = subBaseUrl(url)
        val mHeaders = headers
        val headersMap = mHeaders[subUrl]
        headersMap?.remove(k)
    }

    fun isEmptyHeaders(o_url: String?): Boolean {
        var url = o_url
        url = subBaseUrl(url)
        val heads = getHeaders(url)
        return heads.isNullOrEmpty()
    }

    private fun subBaseUrl(originUrl: String?): String? {
        if (TextUtils.isEmpty(originUrl)) {
            return originUrl
        }
        val index = originUrl!!.indexOf("?")
        return if (index <= 0) {
            originUrl
        } else {
            originUrl.substring(0, index)
        }
    }

    companion object {
        @JvmStatic
        fun create(): HttpHeaders {
            return HttpHeaders()
        }
    }
}
