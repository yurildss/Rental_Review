package com.example.rentalreview.model

data class GeoResponse<T>(
    val data: List<T>
)

data class Country(
    val code: String,
    val name: String
)

data class Region(
    val code: String,
    val name: String
)

data class City(
    val id: Int,
    val name: String
)
