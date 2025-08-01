package com.example.rentalreview.screen.search

import android.util.Log
import com.example.rentalreview.model.Review
import com.example.rentalreview.network.GeoApi
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val reviewRepository: StorageService,
    private val accountService: AccountService
): RentalReviewAppViewModel(){

    init {
        launchCatching {
            val listCountry = GeoApi.retrofitService.getCountry()
            Log.d("Country", listCountry.toString())
        }
    }
}

data class SearchScreenUiState(
    val userId: String = "",
    val reviews: List<Review?> = mutableListOf()
)