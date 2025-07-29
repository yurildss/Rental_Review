package com.example.rentalreview.screen.favorites

import com.example.rentalreview.model.Review
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.screen.home.ReviewUiState
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService
) : RentalReviewAppViewModel() {

    val _uiState = MutableStateFlow(FavoriteScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMyFavsReviews()
    }

    /**
     * Get my reviews from storageService
     */
    fun getMyFavsReviews(){

        launchCatching {
            val userId = accountService.currentUserId
            val myReviews = storageService.getFavoriteReviews(userId)
            val uiStateList: MutableList<ReviewUiState?> = mutableListOf()

            for (review in myReviews){
                uiStateList.add(review?.toReviewUiState())
            }

            _uiState.value = _uiState.value.copy(
                userId = userId,
                reviews = uiStateList
            )

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
 * Data class for my reviews screen
 * @property userId user id
 * @property reviews list of my reviews
 * @property otherReviews list of other users reviews
 * @property showComment show comment section
 * @property showOtherUsersComments show other users comments
 */
data class FavoriteScreenUiState(
    val userId: String = "",
    val reviews: MutableList<ReviewUiState?> = mutableListOf(),
    val otherReviews: List<Review?> = emptyList(),
    val showComment: Boolean = false,
    val showOtherUsersComments: Boolean = false
)