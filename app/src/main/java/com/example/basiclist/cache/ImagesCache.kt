package com.example.basiclist.cache

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.LruCache
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ImagesCache(val context: Context) {

    private lateinit var cachedImages: LruCache<String, Bitmap>
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

        diskCacheLock.withLock {
            diskLruCache = DiskLruCache(context, "cache", DiskLruCacheUtils.DISK_CACHE_SIZE, Bitmap.CompressFormat.PNG, 70)
            diskCacheStarting = false
            diskCacheLockCondition.signalAll()
        }
    }

    fun addImageToCache(url: String, value: Bitmap) {
        val key = url.split("token=")[1]
        if (cachedImages.get(key) == null) {
            cachedImages.put(key, value)
        }
        synchronized(diskCacheLock) {
            diskLruCache?.apply {
                if (!containsKey(key)) {
                    put(key, value)
                }
            }
        }
    }

    private fun getBitmapFromDiskCache(key: String): Bitmap? =
        diskCacheLock.withLock {
            while (diskCacheStarting) {
                try {
                    diskCacheLockCondition.await()
                } catch (e: InterruptedException) {

                }

            }
            return diskLruCache?.getBitmap(key)
        }


    fun getImageFromCache(url: String?): Bitmap? {
        val key = url?.split("token=")?.get(1)
        return if (key != null) {
            cachedImages.get(key) ?: getBitmapFromDiskCache(key)
        } else {
            null
        }
    }

    fun removeImageFromCache(key: String?) {
        cachedImages.remove(key)
    }

    fun clearCache() {
        cachedImages.evictAll()
    }

    companion object {
        private var cache: ImagesCache? = null

        private fun init(context: Context): ImagesCache {
            if (cache == null) {
                cache = ImagesCache(context)
            }
            return cache!!
        }

        fun getInstance(context: Context): ImagesCache =
            cache ?: synchronized(this) {
                cache ?: init(context)
            }
    }

}