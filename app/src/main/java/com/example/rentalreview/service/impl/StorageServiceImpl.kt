package com.example.rentalreview.service.impl

import com.example.rentalreview.model.Comments
import com.example.rentalreview.model.Review
import com.example.rentalreview.service.StorageService
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date
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

    override suspend fun updateLikes(reviewId: String, userId: String) {

        firestore.collection(REVIEWS)
            .document(reviewId)
            .update(LIKES_IDS, FieldValue.arrayUnion(userId))
            .await()
    }

    override suspend fun removeLike(reviewId: String, userId: String) {
        firestore.collection(REVIEWS)
            .document(reviewId)
            .update(LIKES_IDS, FieldValue.arrayRemove(userId))
            .await()
    }

    override suspend fun comment(
        reviewId: String,
        userId: String,
        comment: String
    ) {
        firestore
            .collection(REVIEWS)
            .document(reviewId)
            .update(COMMENTS, FieldValue.arrayUnion(Comments(userId, comment, Date())))
            .await()
    }

    override suspend fun addFavorite(reviewId: String, userId: String) {
        firestore
            .collection(REVIEWS)
            .document(reviewId)
            .update(FAVORITES, FieldValue.arrayUnion(userId))
            .await()
    }

    companion object Collections{
        const val REVIEWS = "reviews"
        const val CREATE_AT = "timestamp"
        const val LIKES_IDS = "likesIds"
        const val COMMENTS = "comments"
        const val FAVORITES = "favoriteIdsUsers"
    }
}
