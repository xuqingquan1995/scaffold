package top.xuqingquan.sample

import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.base.model.datasource.provide.BaseDataSourceProvide
import top.xuqingquan.base.model.entity.Listing
import top.xuqingquan.base.model.entity.NetworkStatus
import top.xuqingquan.base.view.activity.SimpleActivity
import top.xuqingquan.utils.Timber

class MainActivity : SimpleActivity() {

    private val repositoryManager = ScaffoldConfig.getRepositoryManager()
    private lateinit var adapter: BeanAdapter
    private val subjectsList = arrayListOf<Subjects>()

    override fun getLayoutId() = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
        val listing = BaseDataSourceProvide.getListing(BeanDataSource())
        adapter = BeanAdapter {
            listing?.retry?.invoke()
        }
        list.adapter = adapter
        listing?.pagedList?.observe(this, Observer {
            adapter.submitList(it)
        })
        listing?.empty?.observe(this, Observer {
            toast("empty--$it")
        })
        listing?.exception?.observe(this, Observer {
            toast("exception${it.message}")
        })
        listing?.networkState?.observe(this, Observer {
            adapter.setNetworkState(it)
        })
        listing?.refreshState?.observe(this, Observer {
            Timber.d("refreshState===$it")
            swipe_refresh.isRefreshing = it == NetworkStatus.RUNNING
        })
        swipe_refresh.setOnRefreshListener {
            Timber.d("refreshState===1111")
            listing?.refresh?.invoke()
        }
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
