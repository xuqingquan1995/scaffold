package top.xuqingquan.delegate

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by 许清泉 on 2019/4/14 12:58
 * Fragment代理类
 */
interface FragmentDelegate {

    companion object {

        const val FRAGMENT_DELEGATE = "FRAGMENT_DELEGATE"
    }

    /**
     * 当fragment第一次与Activity产生关联时就会调用，以后不再调用
     */
    fun onAttach(f: Fragment, context: Context)

    /**
     * 在onAttach执行完后会立刻调用此方法，通常被用于读取保存的状态值，获取或者初始化一些数据，
     * 但是该方法不执行，窗口是不会显示的，因此如果获取的数据需要访问网络，最好新开线程。
     */
    fun onCreate(f: Fragment, savedInstanceState: Bundle?)

    /**
     * 继onCreateView后就会调用此方法
     */
    fun onViewCreated(f: Fragment, view: View, savedInstanceState: Bundle?)

    /**
     * 在Activity.onCreate方法调用后会立刻调用此方法，表示窗口已经初始化完毕，此时可以调用控件了
     */
    fun onActivityCreate(f: Fragment, savedInstanceState: Bundle?)

    /**
     * 开始执行与控件相关的逻辑代码，如按键点击
     */
    fun onStart(f: Fragment)

    /**
     * 这是Fragment从创建到显示的最后一个回调的方法
     */
    fun onResume(f: Fragment)

    /**
     * 当发生界面跳转时，临时暂停，暂停时间是500ms,0.5s后直接进入下面的onStop方法
     */
    fun onPause(f: Fragment)

    /**
     * 当该方法返回时，Fragment将从屏幕上消失
     */
    fun onStop(f: Fragment)

    /**
     *当fragment状态被保存，或者从回退栈弹出，该方法被调用
     */
    fun onDestroyView(f: Fragment)

    /**
     * 当Fragment不再被使用时，如按返回键，就会调用此方法
     */
    fun onDestroy(f: Fragment)

    /**
     * Fragment生命周期的最后一个方法，执行完后将不再与Activity关联，将释放所有fragment对象和资源
     */
    fun onDetach(f: Fragment)

    /**
     * Return true if the fragment is currently added to its activity.
     */
    fun isAdded(): Boolean

    fun onSaveInstanceState(f: Fragment, outState: Bundle)
}