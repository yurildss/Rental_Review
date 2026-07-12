package com.example.rentalreview.service.impl

import com.example.rentalreview.helper.toReview
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
        return first.documents.map {
            it.toReview()
        }
    }

    override suspend fun getMoreReviews(lastReview: Review): List<Review?> {
        val snapshot = firestore.collection(REVIEWS).orderBy(CREATE_AT).startAfter(lastReview.timestamp).limit(6).get().await()
        return snapshot.documents.map { it.toReview() }
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

    override suspend fun removeFavorite(reviewId: String, userId: String) {
        firestore
            .collection(REVIEWS)
            .document(reviewId)
            .update(FAVORITES, FieldValue.arrayRemove(userId))
            .await()
    }

    override suspend fun findMyReviews(userId: String): List<Review?> {
        val myReviews = firestore.collection(REVIEWS).orderBy(CREATE_AT).whereEqualTo("userId", userId).get().await()
        return myReviews.documents.map { it.toReview() }
    }

    override suspend fun getReviewById(reviewId: String): Review? {
        val doc = firestore.collection(REVIEWS).document(reviewId).get().await()
        return if (doc.exists()) doc.toReview() else null
    }

    override suspend fun updateReview(
        reviewId: String,
        review: Review
    ) {
        firestore.collection(REVIEWS).document(reviewId).set(review).await()
    }

    override suspend fun getFavoriteReviews(userId: String): List<Review?> {
        val result = firestore
            .collection(REVIEWS)
            .whereArrayContains(FAVORITES, userId)
            .get()
            .await()
        return result.documents.map { it.toReview() }
    }

    override suspend fun getReviewsByCity(city: String): List<Review?> {
        val result = firestore
            .collection(REVIEWS)
            .whereEqualTo(CITY, city)
            .get()
            .await()
        return result.documents.map { it.toReview() }
    }

    companion object Collections{
        const val REVIEWS = "reviews"
        const val CREATE_AT = "timestamp"
        const val LIKES_IDS = "likesIds"
        const val COMMENTS = "comments"
        const val FAVORITES = "favoriteIdsUsers"
        const val CITY = "address.city"
    }
}
