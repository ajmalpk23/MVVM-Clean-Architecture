package com.example.test

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import org.chromium.net.CronetEngine
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@HiltAndroidApp
@Module
@InstallIn(SingletonComponent::class)
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initializeCronetEngine()
    }

    companion object {
        var cronetEngine: CronetEngine? = null
        var cronetCallbackExecutorService: ExecutorService? = Executors.newFixedThreadPool(4)
    }

    private fun initializeCronetEngine() {
        val myBuilder = CronetEngine.Builder(this)
        cronetEngine = myBuilder.build()
    }
}