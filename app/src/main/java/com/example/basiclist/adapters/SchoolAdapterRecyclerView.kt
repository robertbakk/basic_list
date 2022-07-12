package com.example.basiclist.adapters

import android.graphics.Bitmap
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.basiclist.MainActivity
import com.example.basiclist.R
import com.example.basiclist.SchoolDialogFragment
import com.example.basiclist.cache.DownloadImageTask
import com.example.basiclist.cache.ImagesCache
import com.example.basiclist.model.School


class SchoolAdapterRecyclerView (var schoolList: List<School>) : RecyclerView.Adapter<SchoolAdapterRecyclerView.SchoolViewHolder>() {

    class SchoolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val location: TextView = itemView.findViewById(R.id.location)



        fun bind (school: School, adapter: SchoolAdapterRecyclerView) {
            val cache: ImagesCache = ImagesCache.getInstance(itemView.context)
            val bm: Bitmap? = cache.getImageFromCache(school.logo_url_thumbnail)

            if (bm != null) {
                image.setImageBitmap(bm)
            } else {
                image.setImageBitmap(null)
                DownloadImageTask(
                    adapter =  adapter,
                    desiredHeight = 100,
                    desiredWidth = 100,
                    context = itemView.context
                ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, school.logo_url_thumbnail)
            }
            name.text = school.district
            location.text = school.state

//            Glide.with(itemView.context)
//                .load(school.logo_url_thumbnail)
//                .into(image)

//            Picasso.get().load(school.logo_url_thumbnail).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.school_item, parent, false)
        return SchoolViewHolder(view)
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {

        holder.bind(schoolList[position], this)

        holder.itemView.setOnClickListener {

//            Toast.makeText(holder.itemView.context, schoolList[position].district,Toast.LENGTH_SHORT).show()
//
//            Snackbar.make(holder.itemView, schoolList[position].district, Snackbar.LENGTH_SHORT).show()
//
//            AlertDialog.Builder(holder.itemView.context)
//                .setTitle("Name of the school")
//                .setMessage(schoolList[position].district)
//                .setPositiveButton(android.R.string.ok, null)
//                .show()

            SchoolDialogFragment(schoolList[position]).show((holder.itemView.context as MainActivity).supportFragmentManager, "dialog")
        }

    }

    override fun getItemCount() = schoolList.size

}