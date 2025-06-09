package com.example.rentalreview.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Comments(
    val userId: String = "",
    val comment: String = "",
    @ServerTimestamp val timestamp: Date? = null,
)
