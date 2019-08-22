@file:JvmName("HttpParseUtils")

package top.xuqingquan.http.log

import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import top.xuqingquan.utils.CharacterUtils
import top.xuqingquan.utils.ZipHelper
import top.xuqingquan.utils.decode
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Created by 许清泉 on 2019/4/14 20:57
 * 网络请求解析
 */
/**
 * 解析响应结果
 *
 * @param response    [Response]
 * @return 解析后的响应结果
 */
fun printResult(response: Response): String? {
    try {
        //读取服务器返回的结果
        val responseBody = response.newBuilder().build().body
        val source = responseBody!!.source()
        source.request(Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer
        //获取content的压缩类型
        val encoding = response
            .headers["Content-Encoding"]
        val clone = buffer.clone()
        //解析response content
        return parseContent(responseBody, encoding, clone)
    } catch (e: Throwable) {
        e.printStackTrace()
        return "{\"error\": \"" + e.message + "\"}"
    }

}

/**
 * 解析服务器响应的内容
 *
 * @param responseBody [ResponseBody]
 * @param encoding     编码类型
 * @param clone        克隆后的服务器响应内容
 * @return 解析后的响应结果
 */
private fun parseContent(responseBody: ResponseBody, encoding: String?, clone: Buffer): String {
    var charset: Charset? = StandardCharsets.UTF_8
    val contentType = responseBody.contentType()
    if (contentType != null) {
        charset = contentType.charset(charset)
    }
    return if (encoding != null && encoding.equals("gzip", ignoreCase = true)) {//content 使用 gzip 压缩
        ZipHelper.decompressForGzip(
            clone.readByteArray(),
            convertCharset(charset!!)
        )//解压
    } else if (encoding != null && encoding.equals("zlib", ignoreCase = true)) {//content 使用 zlib 压缩
        ZipHelper.decompressToStringForZlib(
            clone.readByteArray(),
            convertCharset(charset!!)
        )//解压
    } else {//content 没有被压缩, 或者使用其他未知压缩方式
        clone.readString(charset!!)
    }
}

/**
 * 解析请求服务器的请求参数
 *
 * @param request [Request]
 * @return 解析后的请求信息
 */
fun parseParams(request: Request): String {
    try {
        val body = request.newBuilder().build().body ?: return ""
        val requestbuffer = Buffer()
        body.writeTo(requestbuffer)
        var charset: Charset? = StandardCharsets.UTF_8
        val contentType = body.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        var json = requestbuffer.readString(charset!!)
        json = decode(json, convertCharset(charset))
        return CharacterUtils.jsonFormat(json)
    } catch (e: Throwable) {
        e.printStackTrace()
        return "{\"error\": \"" + e.message + "\"}"
    }
}

/**
 * 是否可以解析
 *
 * @param mediaType [MediaType]
 * @return `true` 为可以解析
 */
fun isParseable(mediaType: MediaType?) =
    if (mediaType?.type == null) false else isText(mediaType) || isPlain(
        mediaType
    ) || isJson(mediaType) || isForm(
        mediaType
    ) || isHtml(mediaType) || isXml(
        mediaType
    )

fun isText(mediaType: MediaType?) = if (mediaType?.type == null) false else mediaType.type == "text"

fun isPlain(mediaType: MediaType?) =
    if (mediaType?.subtype == null) false else mediaType.subtype.toLowerCase().contains("plain")

fun isJson(mediaType: MediaType?) =
    if (mediaType?.subtype == null) false else mediaType.subtype.toLowerCase().contains("json")

fun isXml(mediaType: MediaType?) =
    if (mediaType?.subtype == null) false else mediaType.subtype.toLowerCase().contains("xml")

fun isHtml(mediaType: MediaType?) =
    if (mediaType?.subtype == null) false else mediaType.subtype.toLowerCase().contains("html")

fun isForm(mediaType: MediaType?) =
    if (mediaType?.subtype == null) false else mediaType.subtype.toLowerCase().contains("x-www-form-urlencoded")

fun convertCharset(charset: Charset): String {
    val s = charset.toString()
    val i = s.indexOf("[")
    return if (i == -1) s else s.substring(i + 1, s.length - 1)
}