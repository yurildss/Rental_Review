package com.example.rentalreview.screen.review

import androidx.lifecycle.SavedStateHandle
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.Comments
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
class OpenReviewScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService,
    savedStateHandle: SavedStateHandle
): RentalReviewAppViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState = _uiState.asStateFlow()

    private val idReview: String = checkNotNull(savedStateHandle["idReview"])

    init {
        getReviewById()
    }

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
                imageUrl = review?.imageUri ?: mutableListOf(),
                userId = accountService.currentUserId
            )
        }
    }

    fun likeReview() {
        launchCatching {
            val currentUserId = accountService.currentUserId
            val reviewId = _uiState.value.id

            if (_uiState.value.likesIds.contains(currentUserId)) {
                _uiState.update { it.copy(likesIds = it.likesIds.toMutableList().apply { remove(currentUserId) }) }
                withContext(Dispatchers.IO) { storageService.removeLike(reviewId, currentUserId) }
            } else {
                _uiState.update { it.copy(likesIds = it.likesIds.toMutableList().apply { add(currentUserId) }) }
                withContext(Dispatchers.IO) { storageService.updateLikes(reviewId, currentUserId) }
            }
        }
    }

    fun addFavorite() {
        launchCatching {
            val currentUserId = accountService.currentUserId
            val reviewId = _uiState.value.id

            if (_uiState.value.favoriteIds.contains(currentUserId)) {
                _uiState.update { it.copy(favoriteIds = it.favoriteIds.toMutableList().apply { remove(currentUserId) }) }
                withContext(Dispatchers.IO) { storageService.removeFavorite(reviewId, currentUserId) }
            } else {
                _uiState.update { it.copy(favoriteIds = it.favoriteIds.toMutableList().apply { add(currentUserId) }) }
                withContext(Dispatchers.IO) { storageService.addFavorite(reviewId, currentUserId) }
            }
        }
    }

    fun onCommentChange(comment: String) {
        _uiState.update { it.copy(commentEntry = comment) }
    }

    fun addNewComment() {
        launchCatching {
            val currentUserId = accountService.currentUserId
            val reviewId = _uiState.value.id
            val commentText = _uiState.value.commentEntry

            if (commentText.isNotBlank()) {
                val newComment = Comments(currentUserId, commentText, Date())
                _uiState.update {
                    it.copy(
                        comments = it.comments.toMutableList().apply { add(newComment) },
                        commentEntry = ""
                    )
                }
                withContext(Dispatchers.IO) {
                    storageService.comment(reviewId, currentUserId, commentText)
                }
            }
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
    val showComment: Boolean = true,
    val imageUrl: List<String> = mutableListOf(),
    val userId: String = "",
    val commentEntry: String = ""
)
