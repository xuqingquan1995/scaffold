package top.xuqingquan.sample

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.base.model.entity.Listing
import top.xuqingquan.base.model.entity.NetworkStatus
import top.xuqingquan.base.view.activity.SimpleActivity
import top.xuqingquan.base.view.adapter.listadapter.SimplePagedListAdapter
import top.xuqingquan.http.log.Level
import top.xuqingquan.utils.Timber
import top.xuqingquan.utils.anko.startActivity
import top.xuqingquan.utils.anko.toast
import top.xuqingquan.web.nokernel.WebConfig

class MainActivity : SimpleActivity() {

    init {
        ScaffoldConfig.getInstance(application)
            .setBaseUrl("https://api.douban.com")
            .setLevel(Level.NONE)
    }

    private lateinit var adapter: BeanAdapter
    private val config = ScaffoldConfig.getPagedListConfig()

    override fun getLayoutId() = R.layout.activity_main

//    override fun initData(savedInstanceState: Bundle?) {
//        launch {
//            toast("X5初始化中")
//            Timber.d("WebConfig.hasX5()===${WebConfig.hasX5()}")
//            var i = 0
//            while (!WebConfig.hasX5() && i < 60) {
//                delay(1000)
//                i++
//                Timber.d("WebConfig.hasX5()===${WebConfig.hasX5()}")
//            }
//            toast("X5初始化---${WebConfig.hasX5()}")
//            startActivity<HtmlPrinterActivity>()
//            finish()
//        }
//    }

        override fun initData(savedInstanceState: Bundle?) {
        val factory = BeanDataSourceFactory()
        val listing = Listing(
            pagedList = factory.toLiveData(config),
            networkState = Transformations.switchMap(factory.sourceLiveData) { it.networkState },
            refreshState = Transformations.switchMap(factory.sourceLiveData) { it.initialLoad },
            empty = Transformations.switchMap(factory.sourceLiveData) { it.empty },
            refresh = { factory.sourceLiveData.value?.invalidate() },
            retry = { factory.sourceLiveData.value?.retryAllFailed() },
            exception = Transformations.switchMap(factory.sourceLiveData) { it.exception }
        )
        adapter = BeanAdapter {
            listing.retry.invoke()
        }
        list.adapter = adapter
        listing.pagedList.observe(this, Observer {
            adapter.submitList(it)
            Timber.d("adapter.currentList?.size===>${adapter.currentList?.size}")
            adapter.currentList?.forEach { sub ->
                Timber.d("adapter.currentList$sub")
            }
        })
        listing.empty.observe(this, Observer {
            toast("empty--$it")
        })
        listing.exception.observe(this, Observer {
            toast("exception${it.message}")
        })
        listing.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
        listing.refreshState.observe(this, Observer {
            Timber.d("refreshState===$it")
            swipe_refresh.isRefreshing = it == NetworkStatus.RUNNING
        })
        swipe_refresh.setOnRefreshListener {
            Timber.d("refreshState===1111")
            listing.refresh.invoke()
        }
        adapter.listener = object : SimplePagedListAdapter.OnViewClickListener<Subjects>() {
            override fun onClick(view: View, position: Int, data: Subjects?, viewType: Int) {
                toast("onClick---data===>${data?.title}")
                startActivity<WebActivity>(
                    if (position == 0) {
                        "url" to "http://debugtbs.qq.com"
                    } else {
                        "data" to ""
                    }
                )
            }

            override fun onLongClick(view: View, position: Int, data: Subjects?, viewType: Int): Boolean {
                toast("onLongClick---data===>${data?.title}")
                return super.onLongClick(view, position, data, viewType)
            }
        }
    }
}
