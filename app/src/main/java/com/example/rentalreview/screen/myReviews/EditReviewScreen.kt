package com.example.rentalreview.screen.myReviews

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalreview.common.SnackbarManager
import com.example.rentalreview.common.SnackbarMessage
import com.example.rentalreview.screen.review.ReviewEntryForm
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewEditEntryScreen(
    onSaved: () -> Unit = {},
    viewModel: EditReviewViewModel = hiltViewModel(),
){
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage by SnackbarManager.snackbarMessages.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let {
            val message = when (it) {
                is SnackbarMessage.StringSnackbar -> it.message
                is SnackbarMessage.ResourceSnackbar -> context.getString(it.message)
            }
            coroutineScope.launch {
                snackBarHostState.showSnackbar(message)
                SnackbarManager.clearSnackbarMessage()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var star = uiState.rating

    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        LazyColumn {
            item {
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
                    onStreetChanged = viewModel::onStreetChanged,
                    onNumberChanged = viewModel::onNumberChanged,
                    onCountrySelected = viewModel::onCountryChanged,
                    expandedCountryOptions = uiState.expandedCountryOptions,
                    onCountryExpandedOptions = viewModel::onCountryExpandedOptions,
                    countryList = uiState.listOfCountries,
                    onStateExpandedOptions = viewModel::onStateExpandedOptions,
                    onCityExpandedOptions = viewModel::onCityExpandedOptions,
                    onStateSelected = viewModel::onStateSelected,
                    onCitySelected = viewModel::onCitySelected,
                    onImageSelect = {},
                    imageGallery = uiState.imageGallery
                )
            }
        }
    }
}