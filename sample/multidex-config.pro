-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View

-keep class androidx.lifecycle.ProcessLifecycleOwnerInitializer
-keep class androidx.core.app.CoreComponentFactory
-keep class androidx.lifecycle.LifecycleDispatcher
-keep class androidx.lifecycle.ProcessLifecycleOwner

-keep class * implements android.app.Application.ActivityLifecycleCallbacks
-keep class * extends androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks.FragmentLifecycleCallbacks

#base
-keep class top.xuqingquan.**{*;}
-keep class * implements top.xuqingquan.integration.LifecycleConfig

#tbs
-keep class com.tencent.smtt.**{*;}

#debug
-keep class leakcanary.**{*;}