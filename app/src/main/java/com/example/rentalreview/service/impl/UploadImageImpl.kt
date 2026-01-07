package com.example.rentalreview.service.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.rentalreview.service.UploadImageService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class UploadImageImpl @Inject constructor(): UploadImageService {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun uploadImage(imageUri: Uri): String =
        kotlinx.coroutines.suspendCancellableCoroutine { cont ->

            val requestId = MediaManager.get().upload(imageUri)
                .unsigned("reviewsImages")
                .callback(object : UploadCallback {

                    override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                        val imageUrl = resultData["secure_url"] as? String
                        if (imageUrl != null) cont.resume(imageUrl) {}
                        else cont.resumeWithException(IllegalStateException("secure_url ausente"))
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        cont.resumeWithException(Exception(error?.description ?: "Upload error"))
                    }

                    override fun onStart(requestId: String?) {}
                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
                })
                .dispatch()

            cont.invokeOnCancellation {
                // Opcional: cancelar upload pelo requestId, se você guardar/usar a API do Cloudinary.
            }
        }
}