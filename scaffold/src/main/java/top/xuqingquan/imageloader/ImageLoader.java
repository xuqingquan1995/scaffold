package top.xuqingquan.imageloader;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;
import top.xuqingquan.app.ScaffoldConfig;

/**
 * Created by 许清泉 on 2019/4/14 22:38
 */
@SuppressWarnings("unchecked")
public class ImageLoader {
    @Nullable
    private BaseImageLoaderStrategy mStrategy;
    private static ImageLoader instance;

    private ImageLoader() {
        mStrategy = ScaffoldConfig.getLoaderStrategy();
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                instance = new ImageLoader();
            }
        }
        return instance;
    }

    /**
     * 加载图片
     */
    public <T extends ImageConfig> void loadImage(Context context, T config) {
        Preconditions.checkNotNull(mStrategy, "Please implement BaseImageLoaderStrategy and call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of LifecycleConfig");
        this.mStrategy.loadImage(context, config);
    }

    /**
     * 停止加载或清理缓存
     */
    public <T extends ImageConfig> void clear(Context context, T config) {
        Preconditions.checkNotNull(mStrategy, "Please implement BaseImageLoaderStrategy and call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of LifecycleConfig");
        this.mStrategy.clear(context, config);
    }

    /**
     * 可在运行时随意切换 {@link BaseImageLoaderStrategy}
     */
    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        Preconditions.checkNotNull(strategy, "strategy == null");
        this.mStrategy = strategy;
    }

    @Nullable
    public BaseImageLoaderStrategy getLoadImgStrategy() {
        return mStrategy;
    }
}
