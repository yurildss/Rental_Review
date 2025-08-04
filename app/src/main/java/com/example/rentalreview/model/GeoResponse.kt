package com.example.rentalreview.model


data class Country(
    val name: String,
    val iso2: String,
    val iso3: String
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
