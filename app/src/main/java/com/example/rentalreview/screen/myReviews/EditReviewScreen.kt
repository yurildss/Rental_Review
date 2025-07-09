package com.example.rentalreview.screen.myReviews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalreview.screen.review.ReviewEntryForm
import com.example.rentalreview.screen.review.ReviewScreenViewModel

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
        onSaved = { viewModel.onSave(onSaved) },
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
        onStreeetChanged = viewModel::onStreetChanged,
        onNumberChanged = viewModel::onNumberChanged,
        onCityChanged = viewModel::onCityChanged,
        onStateChanged = viewModel::onStateChanged,
        onZipChanged = viewModel::onZipChanged,
        onCountryChanged = viewModel::onCountryChanged
    )
}