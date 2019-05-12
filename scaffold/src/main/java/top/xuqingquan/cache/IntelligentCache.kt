package top.xuqingquan.cache

import androidx.core.util.Preconditions

/**
 * Created by 许清泉 on 2019/4/14 17:57
 */
class IntelligentCache<V>(size: Int) : Cache<String, V> {

    private val mMap: MutableMap<String, V>//可将数据永久存储至内存中的存储容器
    private val mCache: Cache<String, V>//当达到最大容量时可根据 LRU 算法抛弃不合规数据的存储容器

    init {
        mMap = HashMap()
        mCache = LruCache(size)
    }

    /**
     * 将 {@link #mMap} 和 {@link #mCache} 的 {@code size} 相加后返回
     *
     * @return 相加后的 {@code size}
     */
    @Synchronized
    override fun size() = mMap.size + mCache.size()

    /**
     * 将 {@link #mMap} 和 {@link #mCache} 的 {@code maxSize} 相加后返回
     *
     * @return 相加后的 {@code maxSize}
     */
    @Synchronized
    override fun getMaxSize() = mMap.size + mCache.getMaxSize()

    /**
     * 如果在 {@code key} 中使用 {@link #KEY_KEEP} 作为其前缀, 则操作 {@link #mMap}, 否则操作 {@link #mCache}
     *
     * @param key {@code key}
     * @return {@code value}
     */
    @Synchronized
    override fun get(key: String): V? {
        return if (key.startsWith(KEY_KEEP)) {
            mMap[key]
        } else mCache.get(key)
    }

    /**
     * 如果在 {@code key} 中使用 {@link #KEY_KEEP} 作为其前缀, 则操作 {@link #mMap}, 否则操作 {@link #mCache}
     *
     * @param key   {@code key}
     * @param value {@code value}
     * @return 如果这个 {@code key} 在容器中已经储存有 {@code value}, 则返回之前的 {@code value} 否则返回 {@code null}
     */
    @Synchronized
    override fun put(key: String, value: V): V? {
        return if (key.startsWith(KEY_KEEP)) {
            mMap.put(key, value)
        } else {
            mCache.put(key, value)
        }
    }

    /**
     * 如果在 {@code key} 中使用 {@link #KEY_KEEP} 作为其前缀, 则操作 {@link #mMap}, 否则操作 {@link #mCache}
     *
     * @param key {@code key}
     * @return 如果这个 {@code key} 在容器中已经储存有 {@code value} 并且删除成功则返回删除的 {@code value}, 否则返回 {@code null}
     */
    @Synchronized
    override fun remove(key: String): V? {
        return if (key.startsWith(KEY_KEEP)) {
            mMap.remove(key)
        } else {
            mCache.remove(key)
        }
    }

    /**
     * 如果在 {@code key} 中使用 {@link #KEY_KEEP} 作为其前缀, 则操作 {@link #mMap}, 否则操作 {@link #mCache}
     *
     * @param key {@code key}
     * @return {@code true} 为在容器中含有这个 {@code key}, 否则为 {@code false}
     */
    @Synchronized
    override fun containsKey(key: String): Boolean {
        return if (key.startsWith(KEY_KEEP)) {
            mMap.containsKey(key)
        } else {
            mCache.containsKey(key)
        }
    }


    /**
     * 将 {@link #mMap} 和 {@link #mCache} 的 {@code keySet} 合并返回
     *
     * @return 合并后的 {@code keySet}
     */
    @Synchronized
    override fun keySet(): MutableSet<String> {
        val set = mCache.keySet()
        set.addAll(mMap.keys)
        return set
    }

    /**
     * 清空 {@link #mMap} 和 {@link #mCache} 容器
     */
    override fun clear() {
        mCache.clear()
        mMap.clear()
    }

    companion object {
        private const val KEY_KEEP = "Keep="

        /**
         * 使用此方法返回的值作为 key, 可以将数据永久存储至内存中
         *
         * @param key `key`
         * @return Keep= + `key`
         */
        @JvmStatic
        fun getKeyOfKeep(key: String): String {
            Preconditions.checkNotNull(key, "key == null")
            return KEY_KEEP + key
        }
    }
}