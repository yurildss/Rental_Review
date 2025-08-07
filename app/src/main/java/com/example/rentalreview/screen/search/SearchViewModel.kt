package com.example.rentalreview.screen.search

import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.Review
import com.example.rentalreview.model.State
import com.example.rentalreview.network.GeoApi
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.screen.home.ReviewUiState
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    fun updateExpandedStateOptions(){
        _uiState.value = _uiState.value.copy(
            expandedStateOptions = !_uiState.value.expandedStateOptions
        )
    }

    fun onSelectCountryItem(item: Country){
        _uiState.value = _uiState.value.copy(selectedCountryItem = item)
        getStates()
    }

    fun onSelectStateItem(item: State){
        _uiState.value = _uiState.value.copy(selectedStateItem = item)
        getCities()
    }

    fun getStates(){
        launchCatching {
            val listState = GeoApi.retrofitService.getState(_uiState.value.selectedCountryItem.iso2)
            _uiState.value = _uiState.value.copy(states = listState)
        }
    }

    fun updateExpandedCityOptions(){
        _uiState.value = _uiState.value.copy(
            expandedCityOptions = !_uiState.value.expandedCityOptions
        )
    }

    fun onSelectCityItem(item: City){
        _uiState.value = _uiState.value.copy(selectedCityItem = item)
    }

    fun getCities(){
        launchCatching {
            val listCity = GeoApi.retrofitService.getCities(
                _uiState.value.selectedCountryItem.iso2,
                _uiState.value.selectedStateItem.iso2
            )
            _uiState.value = _uiState.value.copy(cities = listCity)
        }
    }

    fun onSearch(){
        launchCatching {
            val reviews = reviewRepository.getReviewsByCity(_uiState.value.selectedCityItem.name)

            val uiStateList: MutableList<ReviewUiState?> = mutableListOf()
            for (review in reviews){
                uiStateList.add(review?.toReviewUiState())
            }

            _uiState.value = _uiState.value.copy(reviews = uiStateList)
        }
    }


    /**
     * Add a review to favorites
     * add a user id in a list of favoriteIds
     */

    fun addFavorite(reviewId: String, index: Int) {
        launchCatching {
            val currentUserId = accountService.currentUserId
            val currentList = _uiState.value.reviews

            val updatedReview = currentList[index]?.copy(
                favoriteIds = currentList[index]?.favoriteIds?.toMutableList()?.apply {
                    if (!contains(currentUserId)) add(currentUserId)
                } ?: mutableListOf(currentUserId)
            )

            if (updatedReview != null) {
                val updatedList = currentList.toMutableList().apply {
                    this[index] = updatedReview
                }

                _uiState.update { it.copy(reviews = updatedList) }

                //backend
                reviewRepository.addFavorite(reviewId, currentUserId)
            }
        }
    }

    /***
     * Convert to data class ReviewUiState
     */
    fun Review.toReviewUiState(): ReviewUiState {
        return ReviewUiState(
            id = id,
            title = title,
            rating = rating,
            review = review,
            type = type,
            startDate = startDate,
            endDate = endDate,
            address = address,
            likesIds = likesIds,
            comments = comments,
            timestamp = timestamp
        )
    }
}

data class SearchScreenUiState(
    val userId: String = "",
    val reviews: List<ReviewUiState?> = mutableListOf(),
    val countries: List<Country> = mutableListOf(),
    val cities: List<City> = mutableListOf(),
    val states: List<State> = mutableListOf(),
    val selectedItemIndex: Int = 0,
    val selectedCountryItem: Country = Country("", "", ""),
    val expandedCountryOptions: Boolean = false,
    val selectedStateItem: State = State("", "", ""),
    val expandedStateOptions: Boolean = false,
    val selectedCityItem: City = City(0, ""),
    val expandedCityOptions: Boolean = false
)