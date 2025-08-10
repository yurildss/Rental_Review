package com.example.rentalreview.model

data class Address (
    val street: String = "",
    val number: String = "",
    val city: City,
    val state: State,
    val zip: String = "",
    val country: Country
    )