package top.xuqingquan.di.module;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import top.xuqingquan.cache.Cache;
import top.xuqingquan.cache.CacheType;
import top.xuqingquan.integration.*;
import top.xuqingquan.lifecycle.FragmentLifecycleCallbacksImpl;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许清泉 on 2019/4/14 14:32
 */
@Module
public abstract class AppModule {

    @Singleton
    @Provides
    static Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    @Singleton
    @Provides
    static AppManager provideAppManager(Application application) {
        return AppManager.getAppManager().init(application);
    }

    @Binds
    abstract IRepositoryManager bindRepositoryManager(RepositoryManager repositoryManager);

    @Singleton
    @Provides
    static Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
        return cacheFactory.build(CacheType.getEXTRAS());
    }

    @Binds
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycle(ActivityLifecycle activityLifecycle);

    @Binds
    abstract FragmentManager.FragmentLifecycleCallbacks bindFragmentLifecycle(FragmentLifecycle fragmentLifecycle);

    @Singleton
    @Provides
    static List<FragmentManager.FragmentLifecycleCallbacks> provideFragmentLifecycles() {
        List <FragmentManager.FragmentLifecycleCallbacks> list=new ArrayList<>();
        list.add(new FragmentLifecycleCallbacksImpl());
        return list;
    }

    public interface GsonConfiguration {
        void configGson(@NonNull Context context, @NonNull GsonBuilder builder);
    }

}
