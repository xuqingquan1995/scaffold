package top.xuqingquan.utils

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat

/**
 * Created by 许清泉 on 2019-05-27 00:54
 */
object NetUtils {

    @JvmStatic
    fun checkNetworkType(ctx: Context): Int {
        val context = ctx.applicationContext
        val netType = 0
        //连接管理对象
        val manager = ContextCompat.getSystemService(context, ConnectivityManager::class.java) ?: return netType
        //获取NetworkInfo对象
        val networkInfo = manager.activeNetworkInfo ?: return netType
        return when (networkInfo.type) {
            ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_WIMAX, ConnectivityManager.TYPE_ETHERNET -> 1
            ConnectivityManager.TYPE_MOBILE -> when (networkInfo.subtype) {
                TelephonyManager.NETWORK_TYPE_LTE  // 4G
                    , TelephonyManager.NETWORK_TYPE_HSPAP, TelephonyManager.NETWORK_TYPE_EHRPD -> 2
                TelephonyManager.NETWORK_TYPE_UMTS // 3G
                    , TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_EVDO_B -> 3
                TelephonyManager.NETWORK_TYPE_GPRS // 2G
                    , TelephonyManager.NETWORK_TYPE_EDGE -> 4
                else -> netType
            }

            else -> netType
        }
    }


    @JvmStatic
    fun networkIsConnect(ctx: Context): Boolean {
        val context = ctx.applicationContext
        val connectivity = ContextCompat.getSystemService(context, ConnectivityManager::class.java)
        val info = connectivity?.activeNetworkInfo
        return info != null && info.isConnected
    }
}