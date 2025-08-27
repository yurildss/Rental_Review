package com.example.rentalreview.model

data class Address (
    val street: String = "",
    val number: String = "",
    val city: City = City(0, ""),
    val state: State = State("", "", ""),
    val zip: String = "",
    val country: Country = Country("", "", "")
    )