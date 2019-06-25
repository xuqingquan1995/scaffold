package top.xuqingquan.sample

import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.base.model.datasource.provide.BaseDataSourceProvide
import top.xuqingquan.base.view.activity.SimpleActivity

class MainActivity : SimpleActivity() {

    private val repositoryManager = ScaffoldConfig.getRepositoryManager()
    private lateinit var adapter: BeanAdapter
    private val subjectsList = arrayListOf<Subjects>()

    override fun getLayoutId() = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
        adapter = BeanAdapter()
        list.adapter = adapter
        val listing = BaseDataSourceProvide.getListing(BeanDataSource())
        listing?.pagedList?.observe(this, Observer {
            adapter.submitList(it)
        })
//        CoroutineScope(Dispatchers.IO).launch {//不分页加载
//            val bean=repositoryManager.obtainRetrofitService(DoubanService::class.java)
//                .top250("0b2bdeda43b5688921839c8ecb20399b",0,100)
//            withContext(Dispatchers.Main){
//                subjectsList.addAll(bean.subjects)
//                adapter.notifyDataSetChanged()
//                Timber.d("currentThread=${WebUtils.isUIThread()}")
//                Timber.d("bean==>$bean")
//            }
//        }
    }
}
