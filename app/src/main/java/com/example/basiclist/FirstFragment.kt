package com.example.basiclist

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.basiclist.Constants.FULL_URL
import com.example.basiclist.connection.HttpUrlConnection
import com.example.basiclist.connection.RetrofitConfig
import com.example.basiclist.databinding.FragmentFirstBinding
import com.example.basiclist.model.School
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type


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

    private fun loadHttpUrl() {
        object : AsyncTask<String?, String?, String>() {
            override fun doInBackground(vararg p0: String?): String {
                connection = HttpUrlConnection()
                return "connection"
            }
            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                loadUI(connection.schools)
            }
        }.execute()
    }

    private fun loadVolley() {
        val queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(
            Request.Method.GET, FULL_URL,
            { response ->
                val type: Type = object : TypeToken<Map<String, School>>() {}.type
                val schools: Map<String, School> = Gson().fromJson(response, type)
                loadUI(schools)
            },null)
        queue.add(stringRequest)
    }

    private fun loadOkHttp() {
        val request = okhttp3.Request.Builder().url(FULL_URL).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : okhttp3.Callback {

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body()?.string()
                if (body == null) {
                    return
                }

                val type: Type = object : TypeToken<Map<String, School>>() {}.type
                val schools: Map<String, School> = Gson().fromJson(body, type)

                MainScope().launch {
                    loadUI(schools)
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {

            }
        })
    }

    private fun loadRetrofitOkHttp() {
        val call: Call<Map<String, School>> = RetrofitConfig.apiService.getSchools()
        call.enqueue(object : Callback<Map<String, School>> {
            override fun onResponse(call: Call<Map<String, School>>, response: Response<Map<String, School>>) {
                val schools: Map<String, School>? = response.body()
                if (schools != null) {
                    loadUI(schools)
                }
            }

            override fun onFailure(call: Call<Map<String, School>>, t: Throwable) {

            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        loadHttpUrl()
//
//        loadVolley()
//
//        loadOkHttp()

        loadRetrofitOkHttp()

    }

    fun loadUI(schools: Map<String, School>) {
        val schoolList = schools.map { it.value }
        val adapter = SchoolAdapter(requireContext(), schoolList as ArrayList<School>)
        binding.listView.adapter = adapter
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    val filteredSchools = schoolList.filter { it.district.lowercase().contains(newText.lowercase()) }
                    adapter.arrayList = filteredSchools as ArrayList<School>
                } else {
                    adapter.arrayList = schoolList
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