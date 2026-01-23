package com.example.rentalreview.helper

import com.example.rentalreview.model.Address
import com.example.rentalreview.model.Comments
import com.example.rentalreview.model.Review
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toReview(): Review {
    val imageField = get("imageUri")

    val imageUris = when (imageField) {
        is String -> mutableListOf(imageField)
        is List<*> -> imageField.filterIsInstance<String>().toMutableList()
        else -> mutableListOf()
    }

    val addressMap = get("address") as? Map<*, *>

    return Review(
        id = id,
        timestamp = getTimestamp("timestamp")?.toDate(),
        title = getString("title") ?: "",
        rating = getLong("rating")?.toInt() ?: 0,
        review = getString("review") ?: "",
        type = getString("type") ?: "",
        startDate = getString("startDate") ?: "",
        endDate = getString("endDate") ?: "",
        address = addressMap?.toAddress() ?: Address(),
        likesIds = (get("likesIds") as? List<String>)?.toMutableList() ?: mutableListOf(),
        comments = (get("comments") as? List<Comments>)?.toMutableList() ?: mutableListOf(),
        favoriteIdsUsers = (get("favoriteIdsUsers") as? List<String>)?.toMutableList() ?: mutableListOf(),
        userId = getString("userId") ?: "",
        imageUri = imageUris
    )
}
