# Scaffold

#### 介绍
快速搭建Android项目的基础框架，具体使用可以参考sample

MVP使用示例:[ScaffoldMVP](https://github.com/xuqingquan1995/ScaffoldMVP.git)

MVVM使用示例：[ScaffoldMVVM](https://github.com/xuqingquan1995/ScaffoldMVVM.git)

#### 软件架构
项目参照[ArmsMVP](https://github.com/JessYanCoding/MVPArms.git)进行大体框架的构建，去除其中的Dagger，Rx 相关部分，降低学习成本

从[Fragmentation](https://github.com/YoKeyword/Fragmentation.git)获得灵感，添加Fragment栈视图，可以大致的查看当前Fragment栈，适合单Activity多Fragment项目

根据[AgentWeb](https://github.com/Justson/AgentWeb.git)改造，实现一个简单的可自动切换内核（X5内核和系统内核的）的WebViewView，只要App中有放X5依赖就可以自动使用X5内核

#### 安装教程

` git clone https://github.com/xuqingquan1995/Scaffold.git `
clone 项目后将scaffold作为module引入即可

#### 使用说明

具体使用可以参考sample
##### Application
`Application` 中的生命周期调用`AppLifecycles`的对应方法，当然，也可以不写，因为已经在`ScaffoldInstaller`中实现部分逻辑了，不影响全局使用，
可以在 `Application#onCreate` 调用 `ScaffoldConfig.getInstance(this)`进行全局参数的配置

```
class App : Application() {

    private lateinit var mAppDelegate: AppLifecycles

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mAppDelegate = AppDelegate.getInstance(base!!)
    }

    override fun onCreate() {
        super.onCreate()
        mAppDelegate.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
    }
}
```

##### Activity
对于Activity，可以直接继承 `SimpleActivity`，如果使用 `ScaffoldMVP` 或 `ScaffoldMVVM` 可以继承 `BaseActivity`

##### Fragment
对于Fragment，可以直接继承 `SimpleFragment`，如果使用 `ScaffoldMVP` 或 `ScaffoldMVVM` 可以继承 `BaseFragment`
