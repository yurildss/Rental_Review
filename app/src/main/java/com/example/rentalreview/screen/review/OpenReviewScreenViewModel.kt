package com.example.rentalreview.screen.review

import androidx.lifecycle.SavedStateHandle
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.Comments
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.Date

class OpenReviewScreenViewModel(
    private val storageService: StorageService,
    private val accountService: AccountService,
    savedStateHandle: SavedStateHandle
): RentalReviewAppViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState = _uiState.asStateFlow()

    private val idReview: String = checkNotNull(savedStateHandle["idReview"])

    private fun getReviewById(){
        launchCatching {
            val review = withContext(Dispatchers.IO) {storageService.getReviewById(idReview)}
            _uiState.value = _uiState.value.copy(
                id = review?.id ?: "",
                title = review?.title ?: "",
                rating = review?.rating ?: 0,
                review = review?.review ?: "",
                type = review?.type ?: "",
                startDate = review?.startDate ?: "",
                endDate = review?.endDate ?: "",
                address = review?.address ?: Address(),
                likesIds = review?.likesIds ?: mutableListOf(),
                comments = review?.comments ?: mutableListOf(),
                favoriteIds = review?.favoriteIdsUsers ?: mutableListOf(),
                timestamp = review?.timestamp ?: Date(),
                imageUrl = review?.imageUri ?: mutableListOf()
            )
        }
    }
}

data class ReviewUiState(
    val id: String = "",
    val title: String = "",
    val rating: Int = 0,
    val review: String = "",
    val type: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val address: Address = Address(),
    val likesIds: MutableList<String> = mutableListOf(),
    val comments: MutableList<Comments> = mutableListOf(),
    val favoriteIds: MutableList<String> = mutableListOf(),
    val timestamp: Date? = null,
    val showComment: Boolean = false,
    val imageUrl: List<String> = mutableListOf(),
)