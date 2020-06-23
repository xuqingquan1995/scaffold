package top.xuqingquan.sample

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import kotlinx.android.synthetic.main.activity_main.*
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.base.model.entity.Listing
import top.xuqingquan.base.model.entity.NetworkStatus
import top.xuqingquan.base.view.activity.SimpleActivity
import top.xuqingquan.extension.*
import top.xuqingquan.utils.RomUtils
import top.xuqingquan.utils.Timber
import top.xuqingquan.utils.startActivity

class MainActivity : SimpleActivity() {

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
        Timber.d("rom===>${RomUtils.getRomInfo()},${RomUtils.getVersion()}")
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
//            toast("empty--$it")
        })
        listing.exception.observe(this, Observer {
//            toast("exception${it.message}")
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
        adapter.setOnItemClickListener {
            onClick { view, position, data, viewType ->
                Timber.d("view===>${view.id == R.id.text},view.id=${view.id}")
                startActivity<WebActivity>(
                    if (position == 0) {
                        "url" to "http://debugtbs.qq.com"
                    } else {
                        "data" to ""
                    }
                )
            }
        }
//        adapter.listener = object : OnItemClickListener<Subjects>() {
//            override fun onClick(view: View, position: Int, data: Subjects?, viewType: Int) {
//                Timber.d("view===>${view.id == R.id.text},view.id=${view.id}")
////                toast("onClick---data===>${data?.title}")
//                startActivity<WebActivity>(
//                    if (position == 0) {
//                        "url" to "http://debugtbs.qq.com"
//                    } else {
//                        "data" to ""
//                    }
//                )
//            }
//
//            override fun onLongClick(
//                view: View,
//                position: Int,
//                data: Subjects?,
//                viewType: Int
//            ): Boolean {
////                toast("onLongClick---data===>${data?.title}")
//                return super.onLongClick(view, position, data, viewType)
//            }
//        }
    }
}
