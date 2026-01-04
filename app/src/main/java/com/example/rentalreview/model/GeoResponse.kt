package com.example.rentalreview.model

/**
 * Types of the responses of the API for
 * find cities, states and countries
 */
data class Country(
    val name: String,
    val iso2: String,
    val iso3: String
)

data class State(
    val countryCode: String,
    val name: String,
    val iso2: String
)

data class City(
    val id: Int,
    val name: String
)
