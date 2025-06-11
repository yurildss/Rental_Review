package com.example.rentalreview.screen.home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rentalreview.model.Comments
import com.example.rentalreview.model.Review
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService
) : RentalReviewAppViewModel() {

    val _uiState = MutableStateFlow(FeedScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getInitialReviews()
    }

    fun getInitialReviews(){
        launchCatching {
            _uiState.value = _uiState.value.copy(
                userId = accountService.currentUserId,
                reviews = storageService.getReviews().toMutableList()
            )

            Log.d("FeedScreenViewModel", "getInitialReviews: ${_uiState.value.reviews}")
        }
    }

    fun comment(reviewId: String, index: Int){
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
                storageService.comment(reviewId, accountService.currentUserId, _uiState.value.comment)
                _uiState.update { it.copy(comment = "") }
            }
        }
    }

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
                storageService.updateLikes(reviewId, currentUserId)

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
                storageService.removeLike(reviewId, currentUserId)
            }
        }
    }

    fun onNavItemClicked(item: NavItem) {
        _uiState.update { it.copy(selectedItem = item) }
    }

    fun onCommentChange(comment: String){
        _uiState.update { it.copy(comment = comment) }
    }

    fun onShowCommentChange(showComment: Boolean){
        _uiState.update { it.copy(showComment = showComment) }
    }

    fun getMoreReviews(){
        launchCatching {
            val newReviews = storageService.getMoreReviews(
                _uiState.value.reviews.last()!!
            )// retorna List<Review>

            _uiState.value = _uiState.value.copy(
                reviews = (_uiState.value.reviews + newReviews) as MutableList<Review?>,
            )
        }
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
    val reviews: MutableList<Review?> = mutableListOf(),
    val otherReviews: List<Review?> = emptyList(),
    val comment: String = "",
    val showComment: Boolean = false,
    val showOtherUsersComments: Boolean = false
)

data class NavItem(
    val icon: ImageVector,
    val description: String,
    val testTag: String = ""
)
