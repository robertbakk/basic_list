package com.example.basiclist.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.example.basiclist.adapters.SchoolAdapterRecyclerView
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DownloadImageTask(private val adapter: SchoolAdapterRecyclerView? = null,
                        private val desiredWidth: Int = 0,
                        private val desiredHeight: Int = 0,
                        val context: Context
                        ): AsyncTask<String, Void, Bitmap>() {

    private var inSampleSize = 0

    private var imageUrl: String? = null

    private var image: Bitmap? = null

    private var cache: ImagesCache = ImagesCache.getInstance(context)

    override fun doInBackground(vararg params: String): Bitmap? {
        imageUrl = params[0]
        return getImage(imageUrl!!)
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        if (result != null) {
            cache.addImageToCache(imageUrl!!, result)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun getImage(imageUrl: String): Bitmap? {
        if (cache.getImageFromCache(imageUrl) == null) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inSampleSize = inSampleSize
            try {
                val imageWidth = options.outWidth
                val imageHeight = options.outHeight
                if (imageWidth > desiredWidth || imageHeight > desiredHeight) {
                    inSampleSize += 2
                    getImage(imageUrl)
                } else {
                    options.inJustDecodeBounds = false
                    val url = URL(imageUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    val stream = connection.inputStream
                    image = BitmapFactory.decodeStream(stream, null, options)
                    return image
                }
            } catch (e: Exception) {

            }
        }

        return cache.getImageFromCache(imageUrl)
    }
}