package com.example.rentalreview.screen.review

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentalreview.common.SnackbarManager
import com.example.rentalreview.common.SnackbarMessage
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.Review
import com.example.rentalreview.model.State
import com.example.rentalreview.network.GeoApi
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import com.example.rentalreview.service.UploadImageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReviewScreenViewModel @Inject constructor(
    private val reviewRepository: StorageService,
    private val accountService: AccountService,
    private val uploadImageService: UploadImageService
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

    private val _uploadState = MutableStateFlow(ImageUploadState())

    private lateinit var imageUri: String
    init {
        launchCatching {
            _uiState.value = _uiState.value.copy(userId = accountService.currentUserId)
            val listCountry = withContext(Dispatchers.IO) {  GeoApi.retrofitService.getCountry() }
            _uiState.value = _uiState.value.copy(listOfCountries = listCountry)
        }
    }

    /**
     * Retrieves the list of states for the selected country.
     */
    fun getStates(){
        launchCatching {
            val listState = withContext(Dispatchers.IO) {  GeoApi.retrofitService.getState(_uiState.value.selectedCountryItem.iso2) }
            _uiState.value = _uiState.value.copy(listOfStates = listState)
        }
    }

    /**
     * Retrieves the list of cities for the selected state and country.
     */
    fun getCities(){
        launchCatching {
            val listCity = withContext(Dispatchers.IO) { GeoApi.retrofitService.getCities(
                _uiState.value.selectedCountryItem.iso2,
                _uiState.value.selectedStateItem.iso2
            ) }
            _uiState.value = _uiState.value.copy(listOfCities = listCity)
        }
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

    fun onZipChanged(zip: String){
        _uiState.value = _uiState.value.copy(zip = zip)
    }

    fun onCountryChanged(country: Country){
        _uiState.value = _uiState.value.copy(selectedCountryItem = country)
        getStates()
    }

    fun onCountryExpandedOptions(){
        _uiState.value = _uiState.value.copy(expandedCountryOptions = !_uiState.value.expandedCountryOptions)
    }

    fun onStateExpandedOptions(){
        _uiState.value = _uiState.value.copy(expandedStateOptions = !_uiState.value.expandedStateOptions)
    }

    fun onCityExpandedOptions(){
        _uiState.value = _uiState.value.copy(expandedCityOptions = !_uiState.value.expandedCityOptions)
    }

    fun onStateSelected(state: State){
        _uiState.value = _uiState.value.copy(selectedStateItem = state)
        getCities()
    }

    fun onCitySelected(city: City){
        _uiState.value = _uiState.value.copy(selectedCityItem = city)
    }

    fun onImageSelect(imageGallery: Uri){
        _uiState.value = _uiState.value.copy(imageGallery = imageGallery)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSave(onSaved: () -> Unit = {}){
        launchCatching {

            if(_uiState.value.title.isEmpty()){
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Title cannot be empty"))
                return@launchCatching
            }

            if(_uiState.value.type.isEmpty()){
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Type cannot be empty"))
                return@launchCatching
            }

            if(startDate.value == LocalDate.now()){
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Start date cannot be empty"))
                return@launchCatching
            }

            if(endDate.value == LocalDate.of(2025, 12, 31)){
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("End date cannot be empty"))
                return@launchCatching
            }

            if(_uiState.value.selectedCountryItem == Country("", "", "")){
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Country cannot be empty"))
                return@launchCatching
            }

            if(_uiState.value.selectedStateItem == State("", "", "")){
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("State cannot be empty"))
                return@launchCatching
            }

            if(_uiState.value.selectedCityItem == City(0, "")){
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("City cannot be empty"))
                return@launchCatching
            }

            if(_uiState.value.imageGallery != Uri.EMPTY){
                val uploadResult = runCatching {
                    uploadImageService.uploadImage(_uiState.value.imageGallery)
                }.onSuccess {
                    imageUri = it
                    SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Image uploaded successfully"))
                }.onFailure {
                    SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Image upload failed"))
                    return@launchCatching
                }
            }

            reviewRepository.saveReview(Review(
                title = _uiState.value.title,
                type = _uiState.value.type,
                startDate = _startDate.value?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
                endDate = _endDate.value?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
                rating = _uiState.value.rating,
                review = _uiState.value.review,
                address = _uiState.value.toAddress(),
                userId = uiState.value.userId,
                imageUri = imageUri
            ))

            onSaved()

        }
    }

    fun onRatingChanged(rating: Int){
        _uiState.value = _uiState.value.copy(rating = rating)
    }

    fun ReviewScreenState.toAddress() = Address(
        street = street,
        number = number,
        city = selectedCityItem,
        state = selectedStateItem,
        zip = zip,
        country = selectedCountryItem,
        countryCode = selectedCountryItem.iso2
    )

}

data class ReviewScreenState(
    val title: String = "",
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
    val street: String = "",
    val number: String = "",
    val zip: String = "",
    val selectedItemIndex: Int = 0,
    val selectedCountryItem: Country = Country("", "", ""),
    val expandedCountryOptions: Boolean = false,
    val selectedStateItem: State = State("", "", ""),
    val expandedStateOptions: Boolean = false,
    val selectedCityItem: City = City(0, ""),
    val expandedCityOptions: Boolean = false,
    val userId: String = "",
    val listOfCountries: List<Country> = listOf(),
    val listOfStates: List<State> = listOf(),
    val listOfCities: List<City> = listOf(),
    val imageGallery: Uri = Uri.EMPTY,
)

data class ImageUploadState(
    val imageUrl: String = "",
    val error: Boolean = false,
    val success: Boolean = false
)