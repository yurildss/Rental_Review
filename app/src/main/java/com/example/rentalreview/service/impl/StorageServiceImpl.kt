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

    override suspend fun getReviews(): List<Review?> {
        val first = firestore.collection(REVIEWS).orderBy(CREATE_AT).limit(6).get().await()
        return first.toObjects(Review::class.java)
    }

    override suspend fun getMoreReviews(lastReview: Review): List<Review?> {
        val first = firestore.collection(REVIEWS).orderBy(CREATE_AT).startAfter(lastReview.timestamp).limit(6).get().await()
        return first.toObjects(Review::class.java)
    }

    companion object Collections{
        const val REVIEWS = "reviews"
        const val CREATE_AT = "timestamp"
    }
}
