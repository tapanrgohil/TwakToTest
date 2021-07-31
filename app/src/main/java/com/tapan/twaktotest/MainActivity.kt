package com.tapan.twaktotest

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.tapan.twaktotest.data.service.ImageCacheWorker
import com.tapan.twaktotest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val navController: NavController
        get() = findNavController(R.id.navHostFragment)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        startMediaCache()
        //tring to build cache for images
    }

    private fun startMediaCache() {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<ImageCacheWorker>(15L, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()


        WorkManager
            .getInstance(this)
            .enqueue(uploadWorkRequest)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun init() {
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.apply {
            setSupportActionBar(toolbar)
            val appBarConfiguration = AppBarConfiguration(setOf(R.id.userListFragment)) //handel back button
            toolbar.setupWithNavController(navController, appBarConfiguration)
        }

    }
}