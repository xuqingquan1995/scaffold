package top.xuqingquan.web.agent

import android.Manifest

internal object AgentWebPermissions {
    @JvmStatic
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    @JvmStatic
    val LOCATION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    @JvmStatic
    val STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @JvmStatic
    val ACTION_CAMERA = "Camera"
    @JvmStatic
    val ACTION_LOCATION = "Location"
    @JvmStatic
    val ACTION_STORAGE = "Storage"

}
