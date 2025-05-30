package com.example.rentalreview.screen.review

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentalreview.screen.RentalReviewAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ReviewScreenViewModel @Inject constructor() : RentalReviewAppViewModel() {

    private val _uiState = MutableStateFlow(ReviewScreenState())
    val uiState: StateFlow<ReviewScreenState> = _uiState.asStateFlow()

    private val _startDate = MutableStateFlow<LocalDate?>(null)
    val startDate: StateFlow<LocalDate?> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<LocalDate?>(null)
    val endDate: StateFlow<LocalDate?> = _endDate.asStateFlow()

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

}

data class ReviewScreenState(

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

)