package sa.edu.tuwaiq.jaheztask01

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HungerZoneApplication: Application() {
    companion object {
        // Will be used for Glide
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}