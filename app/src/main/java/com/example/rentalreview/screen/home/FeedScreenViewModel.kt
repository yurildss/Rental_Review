package com.example.rentalreview.screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.Comments
import com.example.rentalreview.model.Review
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService
) : RentalReviewAppViewModel() {

    val _uiState = MutableStateFlow(FeedScreenUiState())
    val uiState = _uiState.asStateFlow()

    val _userUiState = MutableStateFlow(UserUiState())
    val userUiState = _userUiState.asStateFlow()

    init {
        getInitialReviews()
    }

    fun getInitialReviews(){
        launchCatching {

            val reviews = withContext(Dispatchers.IO) { storageService.getReviews().toMutableList() }
            val uiStateList: MutableList<ReviewUiState?> = mutableListOf()

            for (review in reviews){
                uiStateList.add(review?.toReviewUiState())
            }

            _uiState.value = _uiState.value.copy(
                userId = accountService.currentUserId,
                reviews = uiStateList
            )
        }
    }


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
     * Show comments on review card in a list of reviews on
     * a specific position
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
     * Like a review
     * in a list of reviews
     * in a position with given index
     * add a user id to the likesIds list
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
                withContext(Dispatchers.IO) {
                    storageService.addFavorite(reviewId, currentUserId)
                }
            }
        }
    }

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

    fun onNavItemClicked(item: NavItem) {
        _uiState.update { it.copy(selectedItem = item) }
    }

    fun onCommentChange(comment: String){
        _uiState.update { it.copy(comment = comment) }
    }

    fun getMoreReviews(){
        launchCatching {

            val newReviews = withContext(Dispatchers.IO) {
                storageService.getMoreReviews(
                    _uiState.value.reviews.last()?.toReview()!!
                )
            }
            val uiStateList: MutableList<ReviewUiState?> = mutableListOf()
            for (review in newReviews){
                uiStateList.add(review?.toReviewUiState())
            }

            _uiState.value = _uiState.value.copy(
                reviews = (_uiState.value.reviews + uiStateList) as MutableList<ReviewUiState?>,
            )
        }
    }

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
        )
    }

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

data class FeedScreenUiState(
    val userId: String = "",
    val navItems: List<NavItem> = listOf(
        NavItem(
            icon = Icons.Default.Home,
            description = "Home",
            testTag = "homeScreen"
        ),
        NavItem(
            icon = Icons.Default.Search,
            description = "Search",
            testTag = "searchScreen"
        ),
        NavItem(
            icon = Icons.Default.AddCircle,
            description = "AddCircle",
            testTag = "addScreen"
        ),
        NavItem(
            icon = Icons.Default.AccountCircle,
            description = "AccountCircle",
            testTag = "accountScreen"
        )
    ),
    val selectedItem: NavItem = NavItem(
        icon = Icons.Default.Home,
        description = "Home",
        testTag = "homeScreen"
    ),
    val reviews: MutableList<ReviewUiState?> = mutableListOf(),
    val otherReviews: List<Review?> = emptyList(),
    val comment: String = "",
    val showComment: Boolean = false,
    val showOtherUsersComments: Boolean = false
)

data class ReviewUiState(
    val id: String = "",
    val title: String = "",
    val rating: Int = 0,
    val review: String = "",
    val type: String = "",
    val startDate: String = "",
    val endDate: String = "" ,
    val address: Address,
    val likesIds: MutableList<String> = mutableListOf(),
    val comments: MutableList<Comments> = mutableListOf(),
    val favoriteIds: MutableList<String> = mutableListOf(),
    val timestamp: Date? = null,
    val showComment: Boolean = false,
)

data class UserUiState(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val favoriteReviews: MutableList<String> = mutableListOf()
)

data class NavItem(
    val icon: ImageVector,
    val description: String,
    val testTag: String = ""
)
