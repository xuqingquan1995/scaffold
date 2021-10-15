package top.xuqingquan.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import top.xuqingquan.extension.launch
import top.xuqingquan.utils.Timber

class HttpsTestActivity : AppCompatActivity() {
    private var init = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_https_test)
        findViewById<Button>(R.id.url1).setOnClickListener {
            launch {
                if (!init) {
                    val client = ScaffoldConfig.getNewOkHttpClient()
                        .sslSocketFactory(HttpsUtils.sSLSocketFactory, HttpsUtils.trustManager)
                        .build()
                    ScaffoldConfig.getRetrofitMap()["https"] = ScaffoldConfig.getNewRetrofit(client)
                    init = true
                }
                val appList1 = ScaffoldConfig.getRepositoryManager()
                    .setRetrofit("https")
                    .obtainRetrofitService(HttpsService::class.java)
                    .getAppList1()
                Timber.d("appList1=>${appList1}")
            }
        }
        findViewById<Button>(R.id.url2).setOnClickListener {
            launch {
                val appList2 = ScaffoldConfig.getRepositoryManager()
                    .setRetrofit("default")
                    .obtainRetrofitService(HttpsService2::class.java)
                    .getAppList2()
                Timber.d("appList2=>${appList2}")
            }
        }
    }
}