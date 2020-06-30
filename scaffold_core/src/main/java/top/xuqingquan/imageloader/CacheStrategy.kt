package top.xuqingquan.imageloader

import androidx.annotation.IntDef

interface CacheStrategy {
    @IntDef(ALL, NONE, RESOURCE, DATA, AUTOMATIC)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Strategy

    companion object {
        const val ALL = 0
        const val NONE = 1
        const val RESOURCE = 2
        const val DATA = 3
        const val AUTOMATIC = 4
    }
}