package com.example.rentalreview.screen.myReviews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalreview.screen.review.ReviewEntryForm

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewEditEntryScreen(
    onSaved: () -> Unit = {},
    viewModel: EditReviewViewModel = hiltViewModel(),
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var star = uiState.rating

    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()

    ReviewEntryForm(
        uiState = uiState,
        onSaved = { },
        startDate = startDate,
        endDate = endDate,
        star = star,
        updateExpandedOptions = viewModel::updateExpandedOptions,
        typeRental = viewModel::typeRental,
        openDialog = viewModel::openDialog,
        closeDialog = viewModel::closeDialog,
        onRatingChanged = viewModel::onRatingChanged,
        updateReview = viewModel::updateReview,
        onDateRangeSelected = viewModel::onDateRangeSelected,
        onTitleChanged = viewModel::onTitleChanged,
        onStreetChanged = viewModel::onStreetChanged,
        onNumberChanged = viewModel::onNumberChanged,
        onZipChanged = viewModel::onZipChanged,
        onCountrySelected = viewModel::onCountryChanged,
        selectedCountryItem = uiState.selectedCountryItem,
        expandedCountryOptions = uiState.expandedCountryOptions,
        onCountryExpandedOptions = viewModel::onCountryExpandedOptions,
        onStateExpandedOptions = viewModel::onStateExpandedOptions,
        onCityExpandedOptions = viewModel::onCityExpandedOptions,
        onStateSelected = viewModel::onStateChanged,
        onCitySelected = viewModel::onCityChanged,
        countryList = uiState.listOfCountries,
        stateList = uiState.listOfStates,
        cityList = uiState.listOfCities
    )
}