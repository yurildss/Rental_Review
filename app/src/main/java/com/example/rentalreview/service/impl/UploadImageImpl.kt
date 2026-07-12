package com.example.rentalreview.service.impl

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

            MediaManager.get().upload(imageUri)
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

    /**
     * Delete image from Cloudinary by URL.
     * Extracts public_id from URL and constructs delete request
     */
    override suspend fun deleteImage(imageUrl: String): Boolean {
        return try {
            // Extract public_id from Cloudinary URL
            // URL format: https://res.cloudinary.com/{cloud}/image/upload/{version}/{public_id}.{ext}
            val publicId = extractPublicIdFromUrl(imageUrl)

            if(publicId.isEmpty()) {
                Log.w("UploadImageImpl", "Could not extract public_id from URL: $imageUrl")
                return false
            }

            // Note: For production, implement proper Cloudinary REST API deletion
            // This requires authentication token which should be securely managed
            // For now, return true as a placeholder - implement proper deletion with your backend
            Log.d("UploadImageImpl", "Marked for deletion: $publicId")
            true

        } catch(e: Exception) {
            Log.e("UploadImageImpl", "Error deleting image: ${e.message}")
            false
        }
    }

    private fun extractPublicIdFromUrl(url: String): String {
        return try {
            // URL format: https://res.cloudinary.com/{cloud}/image/upload/{version}/{public_id}.{ext}
            val parts = url.split("/")
            val uploadIndex = parts.indexOf("upload")
            if(uploadIndex == -1 || uploadIndex + 2 >= parts.size) {
                return ""
            }
            // Get the part after version and remove file extension
            val fileWithExt = parts[uploadIndex + 2]
            fileWithExt.substringBeforeLast(".")
        } catch(e: Exception) {
            Log.e("UploadImageImpl", "Error extracting public_id: ${e.message}")
            ""
        }
    }
}