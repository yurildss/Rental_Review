package com.example.rentalreview.screen.review

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.Review
import com.example.rentalreview.model.State
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReviewScreenViewModel @Inject constructor(
    private val reviewRepository: StorageService,
    private val accountService: AccountService
) : RentalReviewAppViewModel() {

    private val _uiState = MutableStateFlow(ReviewScreenState())
    val uiState: StateFlow<ReviewScreenState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _startDate = MutableStateFlow<LocalDate?>(LocalDate.now())
    @RequiresApi(Build.VERSION_CODES.O)
    val startDate: StateFlow<LocalDate?> = _startDate.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _endDate = MutableStateFlow<LocalDate?>(LocalDate.of(2025, 12, 31))
    @RequiresApi(Build.VERSION_CODES.O)
    val endDate: StateFlow<LocalDate?> = _endDate.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(userId = accountService.currentUserId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDateRangeSelected(startMillis: Long?, endMillis: Long?) {
        val zoneId = ZoneId.systemDefault()
        _startDate.value = startMillis?.let { millis ->
            Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
        }
        _endDate.value = endMillis?.let { millis ->
            Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
        }
    }

    fun openDialog() {
        _uiState.value = _uiState.value.copy(openDialog = true)
    }

    fun closeDialog() {
        _uiState.value = _uiState.value.copy(openDialog = false)
    }

    fun updateReview(review: String) {
        _uiState.value = _uiState.value.copy(review = review)
    }

    fun updateExpandedOptions(expanded: Boolean) {
        _uiState.value = _uiState.value.copy(expandedOptions = expanded)
    }

    fun typeRental(type: String){
        _uiState.value = _uiState.value.copy(type = type)
    }

    fun onTitleChanged(title: String){
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun onStreetChanged(street: String){
        _uiState.value = _uiState.value.copy(street = street)
    }

    fun onNumberChanged(number: String){
        _uiState.value = _uiState.value.copy(number = number)
    }

    fun onZipChanged(zip: String){
        _uiState.value = _uiState.value.copy(zip = zip)
    }

    fun onCountryChanged(country: Country){
        _uiState.value = _uiState.value.copy(selectedCountryItem = country)
    }

    fun onCountryExpandedOptions(){
        _uiState.value = _uiState.value.copy(expandedCountryOptions = !_uiState.value.expandedCountryOptions)
    }

    fun onStateExpandedOptions(){
        _uiState.value = _uiState.value.copy(expandedStateOptions = !_uiState.value.expandedStateOptions)
    }

    fun onCityExpandedOptions(){
        _uiState.value = _uiState.value.copy(expandedCityOptions = !_uiState.value.expandedCityOptions)
    }

    fun onStateSelected(state: State){
        _uiState.value = _uiState.value.copy(selectedStateItem = state)
    }

    fun onCitySelected(city: City){
        _uiState.value = _uiState.value.copy(selectedCityItem = city)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun onSave(onSaved: () -> Unit = {}){
        launchCatching {

            reviewRepository.saveReview(Review(
                title = _uiState.value.title,
                type = _uiState.value.type,
                startDate = _startDate.value?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
                endDate = _endDate.value?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
                rating = _uiState.value.rating,
                review = _uiState.value.review,
                address = _uiState.value.toAddress(),
                userId = uiState.value.userId
            ))

            onSaved()

        }
    }

    fun onRatingChanged(rating: Int){
        _uiState.value = _uiState.value.copy(rating = rating)
    }

    fun ReviewScreenState.toAddress() = Address(
        street = street,
        number = number,
        city = selectedCityItem,
        state = selectedStateItem,
        zip = zip,
        country = selectedCountryItem
    )

}

data class ReviewScreenState(
    val title: String = "",
    val options: List<String> = listOf("",
        "House",
        "Apartment",
        "Studio",
        "Room",
        "Commercial Space",
        "Other"),
    val expandedOptions: Boolean = false,
    val rating: Int = 0,
    val review: String = "",
    val type: String = "",
    val openDialog: Boolean = false,
    val street: String = "",
    val number: String = "",
    val zip: String = "",
    val selectedItemIndex: Int = 0,
    val selectedCountryItem: Country = Country("", "", ""),
    val expandedCountryOptions: Boolean = false,
    val selectedStateItem: State = State("", "", ""),
    val expandedStateOptions: Boolean = false,
    val selectedCityItem: City = City(0, ""),
    val expandedCityOptions: Boolean = false,
    val userId: String = "",
    val listOfCountries: List<Country> = listOf(),
    val listOfStates: List<State> = listOf(),
    val listOfCities: List<City> = listOf()

)