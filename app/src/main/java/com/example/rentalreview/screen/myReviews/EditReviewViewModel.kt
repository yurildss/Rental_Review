package com.example.rentalreview.screen.myReviews

import android.os.Build
import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import com.example.rentalreview.common.SnackbarManager
import com.example.rentalreview.common.SnackbarMessage
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.Review
import com.example.rentalreview.model.State
import com.example.rentalreview.network.GeoApi
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.screen.review.ReviewScreenState
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
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

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class EditReviewViewModel
@Inject constructor (
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val uploadImageService: com.example.rentalreview.service.UploadImageService,
    savedStateHandle: SavedStateHandle
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

    private val reviewId: String = checkNotNull(savedStateHandle["reviewId"])

    init {
        getReviewById(reviewId)
    }

    /**
     * Receive image selection from UI (list of Uri) and update state
     */
    fun onImageSelect(imageGallery: List<Uri>){
        _uiState.value = _uiState.value.copy(imageGallery = imageGallery)
    }

    fun removeImage(index: Int){
        val current = _uiState.value.imageGallery.toMutableList()
        if(index in 0 until current.size){
            current.removeAt(index)
            _uiState.value = _uiState.value.copy(imageGallery = current)
        }
    }

    /**
     * Helper to check if the form is valid for enabling Save button
     */
    fun isFormValid(startDate: java.time.LocalDate?, endDate: java.time.LocalDate?, state: ReviewScreenState): Boolean{
        if(state.title.isEmpty()) return false
        if(state.type.isEmpty()) return false
        if(startDate == java.time.LocalDate.now()) return false
        if(endDate == java.time.LocalDate.of(2025, 12, 31)) return false
        if(state.selectedCountryItem == com.example.rentalreview.model.Country("", "", "")) return false
        if(state.selectedStateItem == com.example.rentalreview.model.State("", "", "")) return false
        if(state.selectedCityItem == com.example.rentalreview.model.City(0, "")) return false
        return true
    }

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

    fun onCountryExpandedOptions(){
        _uiState.value = _uiState.value.copy(expandedCountryOptions = !_uiState.value.expandedCountryOptions)

    }

    fun onStateExpandedOptions(){
        _uiState.value = _uiState.value.copy(expandedStateOptions = !_uiState.value.expandedStateOptions)
    }

    fun onCityExpandedOptions(){
        _uiState.value = _uiState.value.copy(expandedCityOptions = !_uiState.value.expandedCityOptions)
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

    fun onCityChanged(city: City){
        _uiState.value = _uiState.value.copy(selectedCityItem = city)
    }

    fun onStateChanged(state: State){
        _uiState.value = _uiState.value.copy(selectedStateItem = state)
    }

    fun onZipChanged(zip: String){
        _uiState.value = _uiState.value.copy(zip = zip)
    }

    fun onStateSelected(state: State){
        _uiState.value = _uiState.value.copy(selectedStateItem = state)
        getCities()
    }

    fun onCitySelected(city: City){
        _uiState.value = _uiState.value.copy(selectedCityItem = city)
    }

    fun onCountryChanged(country: Country){
        _uiState.value = _uiState.value.copy(selectedCountryItem = country)
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
        country = selectedCountryItem
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReviewById(reviewId: String){

        launchCatching {

            val review = withContext(Dispatchers.IO) { storageService.getReviewById(reviewId) }

            _uiState.value = _uiState.value.copy(
                title = review?.title ?: "",
                street = review?.address?.street ?: "",
                number = review?.address?.number ?: "",
                selectedCityItem = review?.address?.city ?: City(0, ""),
                selectedStateItem = review?.address?.state ?: State("", "", ""),
                zip = review?.address?.zip ?: "",
                selectedCountryItem = review?.address?.country ?: Country("", "", ""),
                type = review?.type ?: "",
                review = review?.review ?: "",
                userId = review?.userId ?: ""
            )
            _startDate.value = runCatching{ LocalDate.parse(review?.startDate) }.getOrNull()
            _endDate.value = runCatching{ LocalDate.parse(review?.endDate) }.getOrNull()

            // populate image gallery from existing image urls
            val images = review?.imageUri?.mapNotNull {
                runCatching { Uri.parse(it) }.getOrNull()
            } ?: emptyList()
            _uiState.value = _uiState.value.copy(imageGallery = images)

        }

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

            // Handle image uploads: keep existing http(s) URIs, upload local URIs
            // Track newly uploaded URLs for rollback on failure
            val newlyUploadedUrls = mutableListOf<String>()
            var imageUri: MutableList<String> = mutableListOf()
            if(_uiState.value.imageGallery.isNotEmpty()){
                runCatching {
                    _uiState.value.imageGallery.map { uri ->
                        val scheme = uri.scheme ?: ""
                        if(scheme.startsWith("http")) {
                            uri.toString()
                        } else {
                            val uploadedUrl = uploadImageService.uploadImage(uri)
                            newlyUploadedUrls.add(uploadedUrl)
                            uploadedUrl
                        }
                    }
                }.onSuccess { uploadUrls ->
                    imageUri = uploadUrls.toMutableList()
                }.onFailure {
                    SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Image upload failed"))
                    return@launchCatching
                }
            }

            // Try to save review to Firebase
            runCatching {
                storageService.updateReview(reviewId = reviewId, review = Review(
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
            }.onSuccess {
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Review updated successfully"))
                onSaved()
            }.onFailure { error ->
                // Firebase save failed - rollback all newly uploaded images
                Log.e("EditReviewViewModel", "Failed to save review: ${error.message}")
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Failed to save review. Please try again."))

                // Delete all newly uploaded images asynchronously
                launchCatching {
                    rollbackUploadedImages(newlyUploadedUrls)
                }
            }

        }
    }

    /**
     * Rollback uploads by deleting all newly uploaded images from storage.
     * This is called when Firebase save fails to avoid orphaned images.
     */
    private suspend fun rollbackUploadedImages(uploadedUrls: List<String>) {
        val failedDeletions = mutableListOf<String>()

        for(url in uploadedUrls) {
            val result = withContext(Dispatchers.IO) {
                uploadImageService.deleteImage(url)
            }
            if(!result) {
                failedDeletions.add(url)
                Log.w("EditReviewViewModel", "Failed to delete image during rollback: $url")
            }
        }

        if(failedDeletions.isNotEmpty()) {
            Log.e("EditReviewViewModel", "Rollback failed for ${failedDeletions.size} images. Manual cleanup may be needed.")
        } else if(uploadedUrls.isNotEmpty()) {
            Log.d("EditReviewViewModel", "Successfully rolled back ${uploadedUrls.size} uploaded images")
        }
    }

}