package top.xuqingquan.sample

import android.view.View
import top.xuqingquan.base.view.adapter.listadapter.SimpleRecyclerAdapter

/**
 * Create by 许清泉 on 2020/6/22 23:15
 */
class TestAdapter:SimpleRecyclerAdapter<Bean>(arrayListOf()) {

    override fun onClick(view: View, position: Int, data: Bean?, viewType: Int){
    }

}