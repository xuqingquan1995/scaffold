package top.xuqingquan.app

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri
import top.xuqingquan.delegate.AppDelegate
import top.xuqingquan.delegate.AppLifecycles

class ScaffoldInstaller : ContentProvider() {

    private lateinit var mAppDelegate: AppLifecycles

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0

    override fun getType(uri: Uri) = null

    override fun insert(uri: Uri, values: ContentValues?) = null

    override fun onCreate(): Boolean {
        mAppDelegate = AppDelegate.getInstance(context!!)
        mAppDelegate.onCreate(context!!.applicationContext as Application)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ) = null

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ) = 0
}
