package com.example.basiclist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.basiclist.MainActivity
import com.example.basiclist.R
import com.example.basiclist.SchoolDialogFragment
import com.example.basiclist.model.School

class SchoolAdapterListView(private val context: Context, var schoolList: ArrayList<School>) : BaseAdapter() {
    private lateinit var image: ImageView
    private lateinit var name: TextView
    private lateinit var location: TextView
    override fun getCount(): Int {
        return schoolList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.school_item, parent, false)
        name = view.findViewById(R.id.name)
        name.text = schoolList[position].district
        location = view.findViewById(R.id.location)
        location.text = schoolList[position].state
        image = view.findViewById(R.id.image)
        Glide.with(context)
            .load(schoolList[position].logo_url_thumbnail)
            .into(image)

//        Picasso.get().load(arrayList[position].logo_url_thumbnail).into(image)

        view.setOnClickListener {

//            Toast.makeText(context, arrayList[position].district,Toast.LENGTH_SHORT).show()

//            Snackbar.make(view, arrayList[position].district, Snackbar.LENGTH_SHORT).show()

//            AlertDialog.Builder(context)
//                .setTitle("Name of the school")
//                .setMessage(arrayList[position].district)
//                .setPositiveButton(android.R.string.ok, null)
//                .show()

            SchoolDialogFragment(schoolList[position]).show((context as MainActivity).supportFragmentManager, "dialog")
        }
        return view
    }
}