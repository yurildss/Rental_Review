package com.example.rentalreview.service.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.rentalreview.service.UploadImageService
import javax.inject.Inject

class UploadImageImpl @Inject constructor(): UploadImageService {
    override fun uploadImage(
        context: Context,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        MediaManager.get().upload(imageUri)
            .unsigned("reviewsImages")
            .callback(object : UploadCallback {
                override fun onSuccess(
                    requestId: String?,
                    resultData: Map<*, *>
                ) {
                    val imageUrl = resultData["secure_url"] as String
                    onSuccess(imageUrl)
                }

                override fun onError(
                    requestId: String?,
                    error: ErrorInfo?
                ) {
                    onError(Exception(error?.description))
                }

                override fun onStart(requestId: String?) {
                    Log.d("Cloudinary Quickstart", "Upload start");
                }
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                    Log.d("Cloudinary Quickstart", "Upload progress");
                }
                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch()
    }
}