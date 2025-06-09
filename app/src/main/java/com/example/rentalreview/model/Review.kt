package com.example.rentalreview.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import org.w3c.dom.Comment
import java.time.LocalDate
import java.util.Date

data class Review(
    @DocumentId val id: String = "",
    @ServerTimestamp val timestamp: Date? = null,
    val title: String = "",
    val rating: Int = 0,
    val review: String = "",
    val type: String = "",
    val startDate: String = "",
    val endDate: String = "" ,
    val address: String = "",
    val likesIds: MutableList<String> = mutableListOf(),
    val comments: MutableList<Comments> = mutableListOf()
)