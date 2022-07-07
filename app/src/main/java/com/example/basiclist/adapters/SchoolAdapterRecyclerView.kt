package com.example.basiclist.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.basiclist.MainActivity
import com.example.basiclist.R
import com.example.basiclist.SchoolDialogFragment
import com.example.basiclist.model.School
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class SchoolAdapterRecyclerView (var schoolList: List<School>) : RecyclerView.Adapter<SchoolAdapterRecyclerView.SchoolViewHolder>() {

    class SchoolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val location: TextView = itemView.findViewById(R.id.location)


        fun bind (school: School) {
            name.text = school.district
            location.text = school.state

            Glide.with(itemView.context)
                .load(school.logo_url_thumbnail)
                .into(image)

//            Picasso.get().load(school.logo_url_thumbnail).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.school_item, parent, false)
        return SchoolViewHolder(view)
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {

        holder.bind(schoolList[position])

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