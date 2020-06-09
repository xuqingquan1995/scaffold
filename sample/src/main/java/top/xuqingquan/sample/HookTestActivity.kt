package top.xuqingquan.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
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
                longToast("error===>${t.message}")
            }
        }
        btn2.setOnClickListener {
            btn1.isVisible = false
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