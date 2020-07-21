package top.xuqingquan.sample

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import top.xuqingquan.utils.EncryptUtils
import top.xuqingquan.utils.Timber
import java.lang.reflect.Type

/**
 * Created by 许清泉 on 2020/7/18 19:51
 */
class TestConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        Timber.d("responseBodyConverter--type===>${TypeToken.get(type).rawType.simpleName}")
        for (annotation in annotations) {
            Timber.d("responseBodyConverter---annotation===>${annotation.annotationClass::simpleName}")
        }
        return TestResponseBodyConverter(gson, adapter)
    }

    companion object {
        @JvmOverloads
        fun create(gson: Gson? = Gson()): TestConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return TestConverterFactory(gson)
        }
    }

}

class TestResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) :
    Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T? {
        val valueResult = value.string()
        val contentType = value.contentType()
        try {
            val type = object : TypeToken<ApiResult<String>>() {}.type
            val apiResult = gson.fromJson<ApiResult<String>>(valueResult, type)
            if (apiResult.status == 200) {
                val key = "19fe872d1d9da0db".substring(0, 16)
                val transformation = "AES/CBC/PKCS5Padding"
                val iv = "4646a98799a80f54".toByteArray()
                val bytes =
                    EncryptUtils.decryptBase64AES(
                        apiResult.data.toString().toByteArray(),
                        key.toByteArray(), transformation, iv
                    )
                apiResult.data = String(bytes)
                val body = ResponseBody.create(contentType, apiResult.toJson())
                val jsonReader = gson.newJsonReader(body.charStream())
                val result = adapter.read(jsonReader)
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw JsonIOException("JSON document was not fully consumed.")
                }
                body.close()
                return result
            } else {
                throw Exception("返回错误数据")
            }
        } catch (e: Throwable) {
            val body = ResponseBody.create(contentType, valueResult)
            val jsonReader = gson.newJsonReader(body.charStream())
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            return result
        } finally {
            value.close()
        }
    }
}