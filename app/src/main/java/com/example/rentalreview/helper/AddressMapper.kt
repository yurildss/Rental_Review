package com.example.rentalreview.helper
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.State

fun Map<*, *>.toAddress(): Address =
    Address(
        street = this["street"] as? String ?: "",
        number = this["number"] as? String ?: "",
        city = (this["city"] as? Map<*, *>)?.toCity() ?: City(),
        state = (this["state"] as? Map<*, *>)?.toState() ?: State(),
        zip = this["zip"] as? String ?: "",
        country = (this["country"] as? Map<*, *>)?.toCountry() ?: Country(),
        countryCode = this["countryCode"] as? String ?: ""
    )

fun Map<*, *>.toCity() = City(
    id = (this["id"] as? Long)?.toInt() ?: 0,
    name = this["name"] as? String ?: ""
)

fun Map<*, *>.toState() = State(
    name = this["name"] as? String ?: "",
    iso2 = this["iso2"] as? String ?: "",
    countryCode = this["countryCode"] as? String ?: ""
)

fun Map<*, *>.toCountry() = Country(
    name = this["name"] as? String ?: "",
    iso2 = this["iso2"] as? String ?: "",
    iso3 = this["iso3"] as? String ?: ""
)
