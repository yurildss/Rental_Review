package com.example.rentalreview.service

import com.example.rentalreview.model.Review
import com.example.rentalreview.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val currentUserId: String
    val currentUser: Flow<User>

    suspend fun register(email: String, password: String, name: String)
    suspend fun authenticate(email: String, password: String)
    suspend fun logOut()
    suspend fun passwordResetEmail(email: String)
}