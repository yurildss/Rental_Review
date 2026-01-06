package com.example.rentalreview.service

import android.content.Context
import android.net.Uri

interface UploadImageService {

    fun uploadImage(context: Context,
                    imageUri: Uri,
                    onSuccess: (String) -> Unit,
                    onError: (Throwable) -> Unit)
}