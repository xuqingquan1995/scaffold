package top.xuqingquan.sample

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import kotlinx.android.synthetic.main.activity_main.*
import top.xuqingquan.app.ScaffoldConfig
import top.xuqingquan.base.model.entity.Listing
import top.xuqingquan.base.model.entity.NetworkStatus
import top.xuqingquan.base.view.activity.SimpleActivity
import top.xuqingquan.extension.*
import top.xuqingquan.utils.Timber
import top.xuqingquan.utils.startActivity
import top.xuqingquan.utils.toast
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.delay

class MainActivity : SimpleActivity() {

    private lateinit var adapter: BeanAdapter
    private lateinit var adapter1: StringAdapter
    private val config = ScaffoldConfig.getPagedListConfig()

    override fun getLayoutId() = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
        initData2()
        launch {
            delay(5000)
            initData1()
        }
    }

    private fun initData1() {
        val factory = BeanDataSourceFactory()
        val listing = Listing(
            pagedList = LivePagedListBuilder(factory, config).build(),
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
            Timber.d("empty000000")
        })
        listing.exception.observe(this, Observer {
    //            toast("exception${it.message}")
            Timber.d("error000000")
        })
        listing.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
        listing.refreshState.observe(this, Observer {
            Timber.d("refreshState00000===$it")
            swipe_refresh.isRefreshing = it == NetworkStatus.RUNNING
        })
        swipe_refresh.setOnRefreshListener {
            Timber.d("refreshState00000===1111")
            listing.refresh.invoke()
        }
        adapter.setOnItemClickListener {
            onClick { view, position, data, viewType ->
                startActivity<WebActivity>(
                    when (position) {
                        0 -> "url" to "http://debugtbs.qq.com"
                        1 -> "url" to "https://www.fanhuangli.com/c.html"
                        2 -> "url" to "https://m.baidu.com/"
                        3 -> "url" to "https://m.image.so.com/"
                        else -> "url" to "http://m.bilibili.com"
                    }
                )
            }
        }
    }
    private fun initData2() {
        val factory = StringDataSourceFactory()
        val listing = Listing(
            pagedList = LivePagedListBuilder(factory, config).build(),
            networkState = Transformations.switchMap(factory.sourceLiveData) { it.networkState },
            refreshState = Transformations.switchMap(factory.sourceLiveData) { it.initialLoad },
            empty = Transformations.switchMap(factory.sourceLiveData) { it.empty },
            refresh = { factory.sourceLiveData.value?.invalidate() },
            retry = { factory.sourceLiveData.value?.retryAllFailed() },
            exception = Transformations.switchMap(factory.sourceLiveData) { it.exception }
        )
        adapter1 = StringAdapter {
            listing.retry.invoke()
        }
        list1.adapter = adapter1
        listing.pagedList.observe(this, Observer {
            adapter1.submitList(it)
            Timber.d("adapter.currentList?.size===>${adapter1.currentList?.size}")
            adapter1.currentList?.forEach { sub ->
                Timber.d("adapter.currentList$sub")
            }
        })
        listing.empty.observe(this, Observer {
            Timber.d("empty111111")
        })
        listing.exception.observe(this, Observer {
            Timber.d("error1111111111")
        })
        listing.networkState.observe(this, Observer {
            adapter1.setNetworkState(it)
        })
        listing.refreshState.observe(this, Observer {
            Timber.d("refreshState11111===$it")
            swipe_refresh1.isRefreshing = it == NetworkStatus.RUNNING
        })
        swipe_refresh1.setOnRefreshListener {
            Timber.d("refreshState111111===1111")
            listing.refresh.invoke()
        }
        adapter1.setOnItemClickListener {
            onClick { view, position, data, viewType ->
                startActivity<WebActivity>(
                    when (position) {
                        0 -> "url" to "http://debugtbs.qq.com"
                        1 -> "url" to "https://www.fanhuangli.com/c.html"
                        2 -> "url" to "https://m.baidu.com/"
                        3 -> "url" to "https://m.image.so.com/"
                        else -> "url" to "http://m.bilibili.com"
                    }
                )
            }
        }
    }

}
