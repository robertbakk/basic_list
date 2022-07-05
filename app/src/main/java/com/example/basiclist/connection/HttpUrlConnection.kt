package com.example.basiclist.connection

import com.example.basiclist.model.School
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL


class HttpUrlConnection {
    val connection = URL("https://launchpad-169908.firebaseio.com/schools.json").openConnection() as HttpURLConnection
    var type: Type = object : TypeToken<Map<String, School>>() {}.type
    val data = connection.inputStream.bufferedReader().readText()
    val schools: Map<String, School> = Gson().fromJson(data, type)
}