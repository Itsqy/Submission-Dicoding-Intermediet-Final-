package com.rifqi.testpaging3.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifqi.testpaging3.*
import com.rifqi.testpaging3.databinding.ActivityMenuBinding
import com.rifqi.testpaging3.gmaps.GMapsActivity
import com.rifqi.testpaging3.login.dataStore
import com.rifqi.testpaging3.upload.AddStoryActivity


class MenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding
    lateinit var storyAdapter: StoryAdapter


    private val storyViewModel: StoryViewModel by viewModels() {
        StoryViewModelFactory(
            applicationContext
        )
    }

    private val authViewModel: AuthViewModel by viewModels() {
        ViewModelFactory(UserPrefferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMenuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        storyAdapter = StoryAdapter()




        binding?.apply {

            rvStory.layoutManager = LinearLayoutManager(this@MenuActivity)
            rvStory.adapter = storyAdapter
            rvStory.adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )



            authViewModel.getUser().observe(this@MenuActivity) {
                storyViewModel.stories(it.userToken).observe(this@MenuActivity) { story ->
                    Log.d("valueAPI", story.toString())
                    storyAdapter.submitData(lifecycle, story)
                }
                binding.tvWelcomeUser.text = "Halo ${it.userName} , Selamat Datang !"
            }
        }


        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
            finish()
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mapsStory -> {
                startActivity(Intent(this, GMapsActivity::class.java))
                return true
            }
            R.id.logOut -> {
                authViewModel.logOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return true
            }
            else -> return true
        }
    }

    override fun onResume() {
        super.onResume()
        storyAdapter.retry()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}