package top.xuqingquan.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.swift.sandhook.SandHook
import kotlinx.android.synthetic.main.activity_hook_test.*
import top.xuqingquan.utils.longToast
import top.xuqingquan.utils.toast

class HookTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hook_test)
        btn1.setOnClickListener {
            val str = et.text.toString()
            try {
                toast("Integer.parseInt(${str})=${Integer.parseInt(str)}")
            } catch (t: Throwable) {
                longToast("error===>${t.message},str=$str")
            }
        }
        btn2.setOnClickListener {
//            btn1.isVisible = false
//            btn2.isVisible = false
//            btn3.isVisible = true
            SandHook.addHookClass(IntegerHook::class.java)
        }
        btn3.setOnClickListener {
            val str = et.text.toString()
            try {
                toast("Integer.parseInt(${str})=${Integer.parseInt(str)}")
            } catch (t: Throwable) {
                longToast("error===>${t.message}")
            }
        }
    }
}