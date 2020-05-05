# Scaffold 使用说明
## 依赖Scaffold
### 远程依赖：
[ ![Download](https://api.bintray.com/packages/xuqingquan1995/maven/scaffold/images/download.svg?version=2.0.5) ](https://bintray.com/xuqingquan1995/maven/scaffold/2.0.5/link)
```
在项目module下的build.gradle中dependencies里添加
AndroidX:
implementation "top.xuqingquan:scaffold:2.0.5"
support:
implementation "top.xuqingquan:scaffold:2.0.5-support"
```
或

[![](https://jitpack.io/v/xuqingquan1995/Scaffold.svg)](https://jitpack.io/#xuqingquan1995/Scaffold)
```
在项目目录下的build.gradle中的allprojects下的repositories里面添加
maven {
     url "https://jitpack.io"
}
然后再项目module下的build.gradle中dependencies里添加
AndroidX:
implementation 'com.github.xuqingquan1995:Scaffold:2.0.5'
support:
implementation 'com.github.xuqingquan1995:Scaffold:2.0.5-support'
```
### 作为module引用
```
1. git clone https://github.com/xuqingquan1995/Scaffold.git
2. 在自己项目中选择File->New->Import Module 选择clone下来的工程中的scaffold模块
3. 复制clone下来的Scaffold目录下的version.gradle到自己的项目中
4. 在工程目录下的build.gradle最上面添加 apply from: 'version.gradle'
5. 在自己工程模块中的build.gradle中dependencies里添加  implementation project(path: ':scaffold')
```
### 作为gitsubmodule使用
将Scaffold作为子模块添加到自己项目中，方法大致与作为module引用相似

## 使用Scaffold
### 初始化相关配置，包括网络状态，生命周期等
新建一个App，继承于Application
```kotlin
class App : Application() {

    //已经通过ContentProvider方式初始化，这边的初始化可以不用了
    private lateinit var mAppDelegate: AppLifecycles

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mAppDelegate = AppDelegate.getInstance(base!!)
    }

    override fun onCreate() {
        super.onCreate()
        //生命周期初始化
        mAppDelegate.onCreate(this)
        //配置初始化
        ScaffoldConfig.getInstance(this)
            .setBaseUrl("https://api.douban.com")
            .setLevel(Level.NONE)
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
    }
}
```
### Activity的使用
新建一个`Activity`继承于`SimpleActivity`,实现`SimpleActivity`里面的抽象方法
```kotlin
class MainActivity : SimpleActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}
```
### Fragment的使用
新建一个`Fragment`继承于`SimpleFragment`,实现里面的抽象方法
```kotlin
class MainFragment : SimpleFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initView(view: View) {
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}
```
### RecyclerView Adapter的使用
Scaffold有封装了RecyclerView的Adapter，包括最简单的`SimpleRecyclerAdapter`,`SimplePagedListAdapter`,`SimpleListAdapter`以及添加了默认的网络状态处理的`BasePagedListAdapter`,`BaseListAdapter`

> `SimpleRecyclerAdapter`可以持有数据源，适合需要对数据进行修改的时候使用，其余均不持有数据源

> `SimplePagedListAdapter`,`BasePagedListAdapter`适合在使用Paging加载数据的时候使用

> `SimpleListAdapter`,`BaseListAdapter`适合在不需要对数据进行修改的情况下使用，因为没有办法获取到数据源

以上Adapter为方便扩展，都不是使用抽象类，所以可以按需覆写父类里面的函数，正常情况下都需要覆写`getLayoutRes`和`setData`方法
```kotlin
    /**
     * @param viewType 不同的视图类型
     * @return 返回布局id
     */
    override fun getLayoutRes(viewType: Int) = R.layout.item


    /**
     * @param holder 默认的ViewHolder，可以通过holder.getView<TextView>(R.id.text)获取item中的控件
     * @param data 当前item的数据
     * @param position 当前item的position
     */
    override fun setData(holder: BaseViewHolder<Subjects>, data: Subjects?, position: Int) {
    }
```
以上Adapter都需要传入一个泛型，作为每个item的类型，对于有多个数据类型的情况下，可以直接传入一个Any，然后根据itemViewType和数据类型判断

如果Adapter中需要处理点击事件，可以按照需要覆写`onClick`或`onLongClick`，如果adapter需要更`Activity`或者`Fragment`中的控件交互，也可以在`Activity`或`Fragment`里面设置Listener
```kotlin
adapter.listener = object : SimplePagedListAdapter.OnViewClickListener<Subjects>() {
            override fun onClick(view: View, position: Int, data: Subjects?, viewType: Int) {
            }
}
```
### 协程的调用
在`Activity`和`Fragment`里面均有封装了`launch`函数，可以方便的使用协程，以下是一个网络请求的示例
```kotlin
launch {
            val top250 = ScaffoldConfig.getRepositoryManager()
                .obtainRetrofitService(DoubanService::class.java)
                .top250("xxx", 1, 20)
            toast(top250.toString())
        }
```
> 由于使用协程，并且很好的处理了各种相关生命周期相关问题以及线程间切换的问题，可以不再需要进行麻烦的协程切换操作，从上面可以看到，同一个代码块中，即处理了网络请求，还使用了toast

### 项目地址
> Github: https://github.com/xuqingquan1995/Scaffold.git

> Gitee: https://gitee.com/xuqingquan/Scaffold.git

欢迎Star

# License [![license](https://img.shields.io/github/license/xuqingquan1995/Scaffold.svg)](https://github.com/xuqingquan1995/Scaffold/blob/master/LICENSE)

MIT License

Copyright (c) 2020 许清泉

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## 参考借鉴
- [MVPArms](https://github.com/JessYanCoding/MVPArms)
- [AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)
- [AgentWeb](https://github.com/Justson/AgentWeb)
- [Fragmentation](https://github.com/YoKeyword/Fragmentation)
- [QMUI_Android](https://github.com/Tencent/QMUI_Android)