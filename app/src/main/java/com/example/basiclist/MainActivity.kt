package com.example.basiclist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.basiclist.cache.ImagesCache
import com.example.basiclist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cache: ImagesCache = ImagesCache.getInstance(this)
        cache.initializeCache()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.container, FirstFragment()).commit()
    }
}