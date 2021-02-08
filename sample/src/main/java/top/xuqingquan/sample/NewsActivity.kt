package top.xuqingquan.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.thread0.newssdk.core.ui.fragment.TabFragment
import top.xuqingquan.base.view.activity.SimpleActivity

/**
 * Created by 许清泉 on 2020/5/12 14:01
 */
class NewsActivity : SimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_news)
        initData(savedInstanceState)
    }

    private fun initData(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, TabFragment.newInstance() as Fragment).commit()
    }
}