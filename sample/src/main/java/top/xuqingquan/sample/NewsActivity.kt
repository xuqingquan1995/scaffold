package top.xuqingquan.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.thread0.newssdk.core.ui.fragment.NewsFragment
import com.thread0.newssdk.core.ui.fragment.TabFragment
import top.xuqingquan.base.view.activity.SimpleActivity

class NewsActivity : SimpleActivity() {

    override fun getLayoutId()=R.layout.activity_news

    override fun initData(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, TabFragment.newInstance() as Fragment).commit()
    }

}
