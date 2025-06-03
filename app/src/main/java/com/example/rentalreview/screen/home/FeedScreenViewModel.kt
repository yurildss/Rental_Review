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
import com.example.rentalreview.model.Review
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(private val storageService: StorageService) : RentalReviewAppViewModel() {

    val _uiState = MutableStateFlow(FeedScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onNavItemClicked(navItem: NavItem) {
        Log.d("FeedScreenViewModel", "onNavItemClicked: $navItem")
        _uiState.value = _uiState.value.copy(selectedItem = mutableStateOf(navItem))
    }

    init {
        getInitialReviews()
    }

    fun getInitialReviews(){
        launchCatching {
            _uiState.value = _uiState.value.copy(reviews = storageService.getReviews())
        }
    }

    fun getMoreReviews(){
        launchCatching {
            _uiState.value = _uiState.value.copy(reviews = storageService.getMoreReviews(_uiState.value.reviews.last()!!))
        }
    }
}

data class FeedScreenUiState(
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
    val selectedItem: MutableState<NavItem> = mutableStateOf(navItems.first()),
    val reviews: List<Review?> = listOf()
)

data class NavItem(
    val icon: ImageVector,
    val description: String,
    val testTag: String = ""
)
