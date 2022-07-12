package com.example.basiclist.cache

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

object DiskLruCacheUtils {
    const val DISK_CACHE_SIZE = 1024 * 1024 * 10 // 10MB
    const val IO_BUFFER_SIZE = 8 * 1024
    val isExternalStorageRemovable: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Environment.isExternalStorageRemovable()
        } else true

    fun getExternalCacheDir(context: Context): File? {
        if (hasExternalCacheDir()) {
            return context.externalCacheDir
        }

        val cacheDir = "/Android/data/" + context.packageName + "/cache/"
        return File(Environment.getExternalStorageDirectory().path + cacheDir)
    }

    private fun hasExternalCacheDir(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
    }
}