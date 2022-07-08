package com.example.basiclist.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.example.basiclist.adapters.SchoolAdapterRecyclerView
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DownloadImageTask(private val adapter: SchoolAdapterRecyclerView? = null,
                        private val desiredWidth: Int = 0,
                        private val desiredHeight: Int = 0,
                        ): AsyncTask<String, Void, Bitmap>() {

    private var inSampleSize = 0

    private var imageUrl: String? = null

    private var image: Bitmap? = null

    private var cache: ImagesCache = ImagesCache.instance


//    fun DownloadImageTask(
//        cache: ImagesCache,
//        ivImageView: ImageView?,
//        desireWidth: Int,
//        desireHeight: Int
//    ) {
//        this.cache = cache
//        this.ivImageView = ivImageView
//        this.desiredHeight = desireHeight
//        desiredWidth = desireWidth
//    }

    override fun doInBackground(vararg params: String): Bitmap? {
        imageUrl = params[0]
        return getImage(imageUrl!!)
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        if (result != null) {
            cache.addImageToCache(imageUrl, result)
            adapter?.notifyDataSetChanged()
        }
    }
    private fun getImage(imageUrl: String): Bitmap? {
        if (cache.getImageFromCache(imageUrl) == null) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inSampleSize = inSampleSize
            try {
                val url = URL(imageUrl)
                var connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                var stream: InputStream = connection.inputStream
                image = BitmapFactory.decodeStream(stream, null, options)
                val imageWidth = options.outWidth
                val imageHeight = options.outHeight
                if (imageWidth > desiredWidth || imageHeight > desiredHeight) {
                    inSampleSize += 2
                    getImage(imageUrl)
                } else {
                    options.inJustDecodeBounds = false
                    connection = url.openConnection() as HttpURLConnection
                    stream = connection.inputStream
                    image = BitmapFactory.decodeStream(stream, null, options)
                    return image
                }
            } catch (e: Exception) { }
        }

        return image
    }
}