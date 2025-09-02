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

/**
 * ViewModel for the Search screen.
 *
 * Manages the state and logic for searching reviews by geographic location
 * (country, state, city). Handles user selections, dropdown expansions,
 * API calls to retrieve location data, and filtering reviews from [StorageService].
 *
 * Inherits coroutine launching and error handling from [RentalReviewAppViewModel].
 *
 * @property reviewRepository Service responsible for retrieving and updating review data.
 * @property accountService Service responsible for user account data.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val reviewRepository: StorageService,
    private val accountService: AccountService
): RentalReviewAppViewModel(){
    // Holds the mutable UI state internally
    private val _uiState = MutableStateFlow(SearchScreenUiState())
    /**
     * Publicly exposed immutable state for the UI to observe.
     */
    val uiState = _uiState.asStateFlow()

    init {
        launchCatching {
            val listCountry = GeoApi.retrofitService.getCountry()
            _uiState.value = _uiState.value.copy(countries = listCountry)
        }
    }
    /**
     * Updates the selected item index (used for UI purposes like tab or pager).
     */
    fun onSelectedItemIndex(index: Int){
        _uiState.value = _uiState.value.copy(selectedItemIndex = index)

    }
    /**
     * Toggles the dropdown menu for country selection.
     */
    fun updateExpandedOptions(){
        _uiState.value = _uiState.value.copy(
            expandedCountryOptions = !_uiState.value.expandedCountryOptions
        )
    }
    /**
     * Toggles the dropdown menu for state selection.
     */
    fun updateExpandedStateOptions(){
        _uiState.value = _uiState.value.copy(
            expandedStateOptions = !_uiState.value.expandedStateOptions
        )
    }
    /**
     * Handles the selection of a country and triggers state loading.
     */
    fun onSelectCountryItem(item: Country){
        _uiState.value = _uiState.value.copy(selectedCountryItem = item)
        getStates()
    }
    /**
     * Handles the selection of a state and triggers city loading.
     */
    fun onSelectStateItem(item: State){
        _uiState.value = _uiState.value.copy(selectedStateItem = item)
        getCities()
    }
    /**
     * Retrieves the list of states for the selected country.
     */
    fun getStates(){
        launchCatching {
            val listState = GeoApi.retrofitService.getState(_uiState.value.selectedCountryItem.iso2)
            _uiState.value = _uiState.value.copy(states = listState)
        }
    }
    /**
     * Toggles the dropdown menu for city selection.
     */
    fun updateExpandedCityOptions(){
        _uiState.value = _uiState.value.copy(
            expandedCityOptions = !_uiState.value.expandedCityOptions
        )
    }
    /**
     * Handles the selection of a city.
     */
    fun onSelectCityItem(item: City){
        _uiState.value = _uiState.value.copy(selectedCityItem = item)
    }
    /**
     * Retrieves the list of cities for the selected state and country.
     */
    fun getCities(){
        launchCatching {
            val listCity = GeoApi.retrofitService.getCities(
                _uiState.value.selectedCountryItem.iso2,
                _uiState.value.selectedStateItem.iso2
            )
            _uiState.value = _uiState.value.copy(cities = listCity)
        }
    }
    /**
     * Fetches reviews for the selected city and maps them to [ReviewUiState] for UI rendering.
     */
    fun onSearch(){
        launchCatching {
            val reviews = reviewRepository.getReviewsByCity(_uiState.value.selectedCityItem.name)

            val uiStateList: MutableList<ReviewUiState?> = mutableListOf()
            for (review in reviews){
                uiStateList.add(review?.toReviewUiState())
            }

            _uiState.value = _uiState.value.copy(reviews = uiStateList)
            _uiState.value = _uiState.value.copy(successSearching = true)
        }
    }


    /**
     * Adds the current user to the favorites list for a given review.
     *
     * @param reviewId ID of the review to favorite.
     * @param index Index of the review in the UI state list.
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
/**
 * Holds the UI data for the Search screen.
 *
 * Represents all the state needed to render the search feature,
 * including location selections, dropdown states, and fetched reviews.
 *
 * @property userId ID of the current user.
 * @property reviews List of reviews matching the current search filters.
 * @property countries List of available countries.
 * @property cities List of available cities.
 * @property states List of available states.
 * @property selectedItemIndex Index of the currently selected navigation item/tab.
 * @property selectedCountryItem Currently selected country.
 * @property expandedCountryOptions Whether the country dropdown is expanded.
 * @property selectedStateItem Currently selected state.
 * @property expandedStateOptions Whether the state dropdown is expanded.
 * @property selectedCityItem Currently selected city.
 * @property expandedCityOptions Whether the city dropdown is expanded.
 */
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
    val expandedCityOptions: Boolean = false,
    val successSearching: Boolean = false
)