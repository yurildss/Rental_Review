package com.example.rentalreview.service

import com.example.rentalreview.User
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>

}