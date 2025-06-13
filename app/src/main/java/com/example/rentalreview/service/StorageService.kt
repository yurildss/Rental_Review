package com.example.rentalreview.service

import com.example.rentalreview.model.Review

interface StorageService {

    suspend fun saveReview(review: Review)
    suspend fun getReviews(): List<Review?>
    suspend fun getMoreReviews(lastReview: Review): List<Review?>
    suspend fun updateLikes(reviewId: String, userId: String)
    suspend fun removeLike(reviewId: String, userId: String)
    suspend fun comment(reviewId: String, userId: String, comment: String)
    suspend fun addFavorite(reviewId: String, userId: String)
}