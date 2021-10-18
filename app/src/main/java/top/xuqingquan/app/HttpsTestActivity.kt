package top.xuqingquan.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import top.xuqingquan.extension.launch
import top.xuqingquan.utils.Timber

class HttpsTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_https_test)
        findViewById<Button>(R.id.url1).setOnClickListener {
            launch {
                val appList1 = ScaffoldConfig.getRepositoryManager()
                    .obtainRetrofitService("https", HttpsService::class.java)
                    .getAppList1()
                Timber.d("appList1=>${appList1}")
            }
        }
        findViewById<Button>(R.id.url2).setOnClickListener {
            launch {
                val appList2 = ScaffoldConfig.getRepositoryManager()
                    .obtainRetrofitService(HttpsService2::class.java)
                    .getAppList2()
                Timber.d("appList2=>${appList2}")
            }
        }
    }
}