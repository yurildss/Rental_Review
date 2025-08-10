package com.example.rentalreview.screen.myReviews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.State
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.screen.review.ReviewScreenState
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class EditReviewViewModel
@Inject constructor (
    private val storageService: StorageService,
    private val accountService: AccountService,
    savedStateHandle: SavedStateHandle
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

    private val reviewId: String = checkNotNull(savedStateHandle["reviewId"])

    init {
        getReviewById(reviewId)
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

    fun onCityChanged(city: City){
        _uiState.value = _uiState.value.copy(selectedCityItem = city)
    }

    fun onStateChanged(state: State){
        _uiState.value = _uiState.value.copy(selectedStateItem = state)
    }

    fun onZipChanged(zip: String){
        _uiState.value = _uiState.value.copy(zip = zip)
    }

    fun onCountryChanged(country: Country){
        _uiState.value = _uiState.value.copy(selectedCountryItem = country)
    }

    fun onRatingChanged(rating: Int){
        _uiState.value = _uiState.value.copy(rating = rating)
    }

    fun ReviewScreenState.toAddress() = Address(
        street = street,
        number = number,
        city = selectedCityItem.name,
        state = selectedStateItem.name,
        zip = zip,
        country = selectedCountryItem.name
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReviewById(reviewId: String){

        launchCatching {

            val review = storageService.getReviewById(reviewId)

            _uiState.value = _uiState.value.copy(
                title = review?.title ?: "",
                street = review?.address?.street ?: "",
                number = review?.address?.number ?: "",
                selectedCityItem = City(0, review?.address?.city ?: ""),
                selectedStateItem = State(review?.address?.state ?: "", "", ""),
                zip = review?.address?.zip ?: "",
                selectedCountryItem = Country(review?.address?.country ?: "", "", ""),
                type = review?.type ?: "",
                review = review?.review ?: ""
            )
            _startDate.value = runCatching{ LocalDate.parse(review?.startDate) }.getOrNull()
            _endDate.value = runCatching{ LocalDate.parse(review?.endDate) }.getOrNull()

        }

    }

}