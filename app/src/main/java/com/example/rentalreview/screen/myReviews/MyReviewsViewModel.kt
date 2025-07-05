package com.example.rentalreview.screen.myReviews

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import com.example.rentalreview.model.Review
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.screen.home.NavItem
import com.example.rentalreview.screen.home.ReviewUiState
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
/**
 * View model for my reviews screen
 * @param storageService
 * @param accountService
 * @property _uiState
 * @property uiState
 * get initial my reviews from storageService and convert to ReviewUiState
 * to show in the screen
 */
class MyReviewsViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService
) : RentalReviewAppViewModel() {

    val _uiState = MutableStateFlow(ReviewScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMyReviews()
    }

    /**
     * Get my reviews from storageService
     */
    fun getMyReviews(){

        launchCatching {
            val userId = accountService.currentUserId
            val myReviews = storageService.findMyReviews(userId)
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
data class ReviewScreenUiState(
    val userId: String = "",
    val reviews: MutableList<ReviewUiState?> = mutableListOf(),
    val otherReviews: List<Review?> = emptyList(),
    val showComment: Boolean = false,
    val showOtherUsersComments: Boolean = false
)