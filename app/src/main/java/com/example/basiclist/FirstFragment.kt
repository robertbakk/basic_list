package com.example.basiclist

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.example.basiclist.connection.HttpUrlConnection
import com.example.basiclist.databinding.FragmentFirstBinding
import com.example.basiclist.model.School
import java.util.ArrayList

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var connection: HttpUrlConnection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        object : AsyncTask<String?, String?, String>() {
            override fun doInBackground(vararg p0: String?): String {
                connection = HttpUrlConnection()
                return "connection"
            }
            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                val schools = connection.schools.map { it.value }
                loadUI(schools)
            }
        }.execute()

        binding.searchView.setOnClickListener {
            //binding.searchView.onActionViewExpanded()
        }
    }

    fun loadUI(schools: List<School>) {
        val adapter = SchoolAdapter(requireContext(), schools as ArrayList<School>)
        binding.listView.adapter = adapter
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    val filteredSchools = schools.filter { it.district.lowercase().contains(newText.lowercase()) }
                    adapter.arrayList = filteredSchools as ArrayList<School>
                } else {
                    adapter.arrayList = schools
                }
                adapter.notifyDataSetChanged()
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}