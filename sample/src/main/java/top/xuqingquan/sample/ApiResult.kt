package top.xuqingquan.sample

import com.google.gson.annotations.SerializedName

/**
 * Created by 许清泉 on 2019/3/14 10:08
 */
data class ApiResult<T>(
    @SerializedName(value = "status", alternate = ["code"])
    var status: Int = 0,
    var msg: String? = null,
    @SerializedName(value = "data", alternate = ["ret"])
    var data: T? = null
) {
    fun toJson(): String {
        return "{\"status\":$status,\"msg\":\"$msg\", \"data\":$data}"
    }

    override fun toString(): String {
        return toJson()
    }
}
