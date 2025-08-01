package com.example.rentalreview.screen.search

import android.util.Log
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.Review
import com.example.rentalreview.network.GeoApi
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val reviewRepository: StorageService,
    private val accountService: AccountService
): RentalReviewAppViewModel(){

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        launchCatching {
            val listCountry = GeoApi.retrofitService.getCountry()
        }
    }

    fun onSelectedItem(index: Int){
        _uiState.value = _uiState.value.copy(selectedItemIndex = index)
        Log.d("SelectedItem", _uiState.value.selectedItemIndex.toString())
    }

    fun onEndOfListReached(index: Int){
        Log.d("EndOfListReached", index.toString())
    }
}

data class SearchScreenUiState(
    val userId: String = "",
    val reviews: List<Review?> = mutableListOf(),
    val countries: List<Country> = mutableListOf(),
    val selectedItemIndex: Int = 0,
    val selectedItem: String = ""
)