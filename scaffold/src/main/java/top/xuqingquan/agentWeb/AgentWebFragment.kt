package top.xuqingquan.agentWeb


import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.util.Preconditions
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import top.xuqingquan.BuildConfig
import top.xuqingquan.R
import top.xuqingquan.base.view.fragment.SimpleFragment
import top.xuqingquan.databinding.FragmentAgentWebBinding
import top.xuqingquan.di.component.AppComponent
import top.xuqingquan.di.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class AgentWebFragment : SimpleFragment() {

    private lateinit var binding: FragmentAgentWebBinding
    @Inject
    lateinit var mAgentWeb: AgentWeb
    @Inject
    lateinit var mPopupMenu: PopupMenu
    var url: String? = null
        private set
    var title: String? = null
        private set
    private lateinit var appComponent: AppComponent
    lateinit var tvTitle: TextView
    lateinit var menu: ImageView

    override fun getLayoutId() = R.layout.fragment_agent_web

    override fun setupFragmentComponent(appComponent: AppComponent) {
        this.appComponent = appComponent
    }

    override fun initData(savedInstanceState: Bundle?) {
        url = arguments?.getString("url")
        Preconditions.checkNotNull(url, "url must no be null")
        if (BuildConfig.DEBUG) {
            AgentWebConfig.debug()
        }
        DaggerAgentWebComponent.builder().appComponent(appComponent).view(this).build().inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun initView(view: View) {
        title = arguments?.getString("title")
        tvTitle = binding.title
        tvTitle.text = title
        binding.close.setOnClickListener { fragmentManager?.popBackStack() }
        binding.back.setOnClickListener {
            if (!mAgentWeb.back()) {
                fragmentManager?.popBackStack()
            }
        }
        menu = binding.menu
        binding.menu.setOnClickListener {
            mPopupMenu.show()
        }
        pageNavigator(false)
    }

    fun pageNavigator(isVisible: Boolean) {
        binding.back.isVisible = isVisible
        binding.line.isVisible = isVisible
    }

    override fun onResume() {
        super.onResume()
        mAgentWeb.webLifeCycle.onResume()
    }

    override fun onPause() {
        super.onPause()
        mAgentWeb.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb.webLifeCycle.onDestroy()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return mAgentWeb.handleKeyEvent(keyCode, event)
    }

    override fun setData(data: Any?) {
        super.setData(data)
        if (data is Bundle) {
            arguments = data
        }
    }

}
