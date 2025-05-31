package com.example.rentalreview.service.impl

import com.example.rentalreview.model.Review
import com.example.rentalreview.service.StorageService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : StorageService  {

    override suspend fun saveReview(review: Review) {
        firestore.collection(REVIEWS).add(review).await()
    }

    companion object Collections{
        const val REVIEWS = "reviews"
    }
}
