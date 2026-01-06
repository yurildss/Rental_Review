package com.example.rentalreview

import android.app.Application
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RentalReviewApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = mapOf(
            "cloud_name" to "dgfwjjbx8"
        )

        MediaManager.init(this, config)
    }
}