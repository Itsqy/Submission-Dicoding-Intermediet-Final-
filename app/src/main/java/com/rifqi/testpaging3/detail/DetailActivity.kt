package com.rifqi.testpaging3.detail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rifqi.testpaging3.data.remote.ListStoryData
import com.rifqi.testpaging3.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryData>("story") as ListStoryData
        Log.d("detailactivity", story.toString())
        Glide.with(applicationContext)
            .load(story.photoUrl)
            .into(binding.imgStory)

        binding.tvTitleStory.text = story?.name
        binding.tvDescStory.text = story?.description


    }
}