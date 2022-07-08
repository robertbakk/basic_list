package com.example.basiclist.cache

import android.graphics.Bitmap
import android.util.LruCache
import com.bumptech.glide.disklrucache.DiskLruCache
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class ImagesCache {

    private lateinit var cachedImages: LruCache<String, Bitmap>
    private val DISK_CACHE_SIZE = 1024 * 1024 * 10 // 10MB
    private val DISK_CACHE_SUBDIR = "thumbnails"

    private var diskLruCache: DiskLruCache? = null
    private val diskCacheLock = ReentrantLock()
    private val diskCacheLockCondition: Condition = diskCacheLock.newCondition()
    private var diskCacheStarting = true

    fun initializeCache() {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        cachedImages = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }
    }

    fun addImageToCache(key: String?, value: Bitmap?) {
        if (cachedImages.get(key) == null) {
            cachedImages.put(key, value)
        }
    }

    fun getImageFromCache(key: String?): Bitmap? {
        return if (key != null) {
            cachedImages.get(key)
        } else {
            null
        }
    }

    fun removeImageFroCmCache(key: String?) {
        cachedImages.remove(key)
    }

    fun clearCache() {
        cachedImages.evictAll()
    }

    companion object {
        private var cache: ImagesCache? = null
        val instance: ImagesCache
            get() {
                if (cache == null) {
                    cache = ImagesCache()
                }
                return cache!!
            }
    }
}