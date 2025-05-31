package com.example.rentalreview.service

import com.example.rentalreview.model.Review

interface StorageService {

    suspend fun saveReview(review: Review)

}