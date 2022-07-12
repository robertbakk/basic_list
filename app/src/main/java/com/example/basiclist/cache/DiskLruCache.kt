package com.example.basiclist.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Environment
import com.jakewharton.disklrucache.DiskLruCache
import java.io.*

class DiskLruCache(
    context: Context, uniqueName: String, diskCacheSize: Int,
    compressFormat: CompressFormat, quality: Int
) {
    private var mDiskCache: DiskLruCache? = null
    private var mCompressFormat = CompressFormat.JPEG
    private var mCompressQuality = 70

    private fun writeBitmapToFile(bitmap: Bitmap, editor: DiskLruCache.Editor): Boolean {
        var out: OutputStream? = null
        return try {
            out = BufferedOutputStream(editor.newOutputStream(0), DiskLruCacheUtils.IO_BUFFER_SIZE)
            bitmap.compress(mCompressFormat, mCompressQuality, out)
        } finally {
            out?.close()
        }
    }

    private fun getDiskCacheDir(context: Context, uniqueName: String): File {
        val cachePath: String =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() ||
                !DiskLruCacheUtils.isExternalStorageRemovable
            ) DiskLruCacheUtils.getExternalCacheDir(context)!!.path else context.cacheDir.path
        return File(cachePath + File.separator.toString() + uniqueName)
    }

    fun put(key: String, data: Bitmap) {
        var editor: DiskLruCache.Editor? = null
        try {
            editor = mDiskCache?.edit(key)
            if (editor == null) {
                return
            }
            if (writeBitmapToFile(data, editor)) {
                mDiskCache?.flush()
                editor.commit()
            } else {
                editor.abort()
            }
        } catch (e: IOException) {
            try {
                editor?.abort()
            } catch (ignored: IOException) {

            }
        }
    }

    fun getBitmap(key: String): Bitmap? {
        var bitmap: Bitmap? = null
        var snapshot: DiskLruCache.Snapshot? = null
        try {
            snapshot = mDiskCache?.get(key)
            if (snapshot == null) {
                return null
            }
            val input: InputStream = snapshot.getInputStream(0)
            val buffIn = BufferedInputStream(input, DiskLruCacheUtils.IO_BUFFER_SIZE)
            bitmap = BitmapFactory.decodeStream(buffIn)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            snapshot?.close()
        }
        return bitmap
    }

    fun containsKey(key: String?): Boolean {
        var contained = false
        var snapshot: DiskLruCache.Snapshot? = null
        try {
            snapshot = mDiskCache?.get(key)
            contained = snapshot != null
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            snapshot?.close()
        }
        return contained
    }

    fun clearCache() {
        try {
            mDiskCache?.delete()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    val cacheFolder: File
        get() = mDiskCache!!.directory

    companion object {
        private const val APP_VERSION = 1
        private const val VALUE_COUNT = 1
    }

    init {
        try {
            val diskCacheDir: File = getDiskCacheDir(context, uniqueName)
            mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT,
                diskCacheSize.toLong()
            )
            mCompressFormat = compressFormat
            mCompressQuality = quality
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}