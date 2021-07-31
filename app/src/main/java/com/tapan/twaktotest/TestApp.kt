package com.tapan.twaktotest


import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tapan.twaktotest.util.NetworkLiveData
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TestApp : Application(), Configuration.Provider {

    companion object {
        lateinit var INSTANCE: TestApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        NetworkLiveData.init(this)
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

}