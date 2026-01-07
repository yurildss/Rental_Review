package com.example.rentalreview.service

import android.content.Context
import android.net.Uri

interface UploadImageService {

    suspend fun uploadImage(imageUri: Uri): String
}