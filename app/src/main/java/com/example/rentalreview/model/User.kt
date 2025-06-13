package com.example.rentalreview.model

data class User (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val favoriteReviews: MutableList<String> = mutableListOf()
)