package com.example.rentalreview.screen.favorites

import com.example.rentalreview.model.Comments
import com.example.rentalreview.model.Review
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.screen.home.ReviewUiState
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
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
     * Get favorite reviews from storageService
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

    /**
     * Add a new comment to a review
     */
    fun addNewComment(reviewId: String, index: Int){
        launchCatching {
            val currentList = _uiState.value.reviews

            // review with new comment
            val updatedReview = currentList[index]?.copy(
                comments = currentList[index]?.comments?.toMutableList()?.apply {
                    add(Comments(accountService.currentUserId, _uiState.value.comment))
                } ?: mutableListOf()
            )

            // update review with new comment
            if (updatedReview != null) {
                val updatedList = currentList.toMutableList().apply {
                    this[index] = updatedReview
                }

                // update uiState with a updated review
                _uiState.update { it.copy(reviews = updatedList) }

                //backend
                withContext(Dispatchers.IO) {
                    storageService.comment(reviewId, accountService.currentUserId, _uiState.value.comment)
                }
                _uiState.update { it.copy(comment = "") }
            }
        }
    }

    /**
     * Show/hide comments on review card at a specific position
     */
    fun onShowCommentClick(index: Int){
        val currentList = _uiState.value.reviews
        val updatedReview = currentList[index]?.copy(
            showComment = !currentList[index]?.showComment!!
        )
        if (updatedReview != null) {
            val updatedList = currentList.toMutableList().apply {
                this[index] = updatedReview
            }
            _uiState.update { it.copy(reviews = updatedList) }
        }
    }

    /**
     * Like a review - add current user id to likesIds list
     */
    fun likeReview(reviewId: String, index: Int) {
        launchCatching {
            val currentUserId = accountService.currentUserId
            val currentList = _uiState.value.reviews

            val updatedReview = currentList[index]?.copy(
                likesIds = currentList[index]?.likesIds?.toMutableList()?.apply {
                    if (!contains(currentUserId)) add(currentUserId)
                } ?: mutableListOf(currentUserId)
            )

            if (updatedReview != null) {
                val updatedList = currentList.toMutableList().apply {
                    this[index] = updatedReview
                }

                _uiState.update { it.copy(reviews = updatedList) }

                //backend
                withContext(Dispatchers.IO) {
                    storageService.updateLikes(reviewId, currentUserId)
                }
            }
        }
    }

    /**
     * Unlike a review - remove current user id from likesIds list
     */
    fun unlikeReview(reviewId: String, index: Int) {
        launchCatching {
            val currentUserId = accountService.currentUserId
            val currentList = _uiState.value.reviews

            val updatedReview = currentList[index]?.copy(
                likesIds = currentList[index]?.likesIds?.toMutableList()?.apply {
                    if(contains(currentUserId)) remove(currentUserId)
                } ?: mutableListOf()
            )

            if (updatedReview != null) {
                val updatedList = currentList.toMutableList().apply {
                    this[index] = updatedReview
                }

                _uiState.update { it.copy(reviews = updatedList) }

                //backend
                withContext(Dispatchers.IO) {
                    storageService.removeLike(reviewId, currentUserId)
                }
            }
        }
    }

    /**
     * Remove a review from favorites
     */
    fun removeFavorite(reviewId: String, index: Int) {
        launchCatching {
            val currentUserId = accountService.currentUserId
            val currentList = _uiState.value.reviews

            val updatedReview = currentList[index]?.copy(
                favoriteIds = currentList[index]?.favoriteIds?.toMutableList()?.apply {
                    if(contains(currentUserId)) remove(currentUserId)
                } ?: mutableListOf()
            )

            if (updatedReview != null) {
                val updatedList = currentList.toMutableList().apply {
                    this[index] = updatedReview
                }

                _uiState.update { it.copy(reviews = updatedList) }

                //backend
                withContext(Dispatchers.IO) {
                    storageService.removeFavorite(reviewId, currentUserId)
                }
            }
        }
    }

    /**
     * Update comment text input
     */
    fun onCommentChange(comment: String){
        _uiState.update { it.copy(comment = comment) }
    }

    /***
     * Convert Review model to ReviewUiState
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
            timestamp = timestamp,
            favoriteIds = favoriteIdsUsers,
            imageUrl = imageUri
        )
    }

    /***
     * Convert ReviewUiState to Review model
     */
    fun ReviewUiState.toReview(): Review {
        return Review(
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
 * Data class for favorites screen
 * @property userId current user id
 * @property reviews list of favorite reviews
 * @property comment current comment text being typed
 * @property showOtherUsersComments flag to show/hide other users comments
 */
data class FavoriteScreenUiState(
    val userId: String = "",
    val reviews: MutableList<ReviewUiState?> = mutableListOf(),
    val comment: String = "",
    val showOtherUsersComments: Boolean = false
)