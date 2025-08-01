package com.example.rentalreview.model

data class GeoResponse<T>(
    val data: List<T>
)

data class Country(
    val iso2: String,
    val name: String
)

data class State(
    val country_code: String,
    val name: String,
    val iso2: String
)

data class City(
    val id: Int,
    val name: String
)
