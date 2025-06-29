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
class MyReviewsViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService
) : RentalReviewAppViewModel() {

    val _uiState = MutableStateFlow(ReviewScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMyReviews()
    }

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


data class ReviewScreenUiState(
    val userId: String = "",
    val reviews: MutableList<ReviewUiState?> = mutableListOf(),
    val otherReviews: List<Review?> = emptyList(),
    val comment: String = "",
    val showComment: Boolean = false,
    val showOtherUsersComments: Boolean = false
)