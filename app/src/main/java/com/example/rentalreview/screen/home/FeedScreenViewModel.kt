package com.example.rentalreview.screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rentalreview.screen.RentalReviewAppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class FeedScreenViewModel : RentalReviewAppViewModel() {

    val _uiState = MutableStateFlow(FeedScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onNavItemClicked(navItem: NavItem) {
        _uiState.value = _uiState.value.copy(selectedItem = mutableStateOf(navItem))
    }
}

data class FeedScreenUiState(
    val navItems: List<NavItem> = listOf(
        NavItem(
            icon = Icons.Default.Home,
            description = "Home"
        ),
        NavItem(
            icon = Icons.Default.Search,
            description = "Search"
        ),
        NavItem(
            icon = Icons.Default.AddCircle,
            description = "AddCircle"
        ),
        NavItem(
            icon = Icons.Default.AccountCircle,
            description = "AccountCircle"
        )
    ),
    val selectedItem: MutableState<NavItem> = mutableStateOf(navItems.first())
)

data class NavItem(
    val icon: ImageVector,
    val description: String
)
