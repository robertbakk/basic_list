package com.example.basiclist.connection

import com.example.basiclist.Constants.GET_SCHOOLS
import com.example.basiclist.model.School
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitInterface {

    @GET(GET_SCHOOLS)
    fun getSchools(): Call<Map<String, School>>

}