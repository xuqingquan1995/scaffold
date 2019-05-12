package top.xuqingquan.agentWeb

import dagger.BindsInstance
import dagger.Component
import top.xuqingquan.di.component.AppComponent
import top.xuqingquan.di.scope.FragmentScope

/**
 * Created by 许清泉 on 2019-04-29 22:47
 */
@FragmentScope
@Component(modules = [AgentWebModule::class], dependencies = [AppComponent::class])
interface AgentWebComponent {
    fun inject(fragment: AgentWebFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun view(fragment: AgentWebFragment): Builder

        fun appComponent(appComponent: AppComponent): Builder
        fun build(): AgentWebComponent
    }
}