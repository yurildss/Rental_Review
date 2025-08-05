package com.example.rentalreview.screen.search

import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.Review
import com.example.rentalreview.model.State
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
            _uiState.value = _uiState.value.copy(countries = listCountry)

        }
    }

    fun onSelectedItemIndex(index: Int){
        _uiState.value = _uiState.value.copy(selectedItemIndex = index)

    }

    fun updateExpandedOptions(){
        _uiState.value = _uiState.value.copy(
            expandedCountryOptions = !_uiState.value.expandedCountryOptions
        )
    }

    fun onSelectItem(item: Country){
        _uiState.value = _uiState.value.copy(selectedItem = item.name)
    }
}

data class SearchScreenUiState(
    val userId: String = "",
    val reviews: List<Review?> = mutableListOf(),
    val countries: List<Country> = mutableListOf(),
    val cities: List<City> = mutableListOf(),
    val states: List<State> = mutableListOf(),
    val selectedItemIndex: Int = 0,
    val selectedItem: String = "",
    val expandedCountryOptions: Boolean = false,
)