package com.example.rentalreview.screen.perfil

import androidx.compose.runtime.mutableStateOf
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
class ProfileViewModel
@Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService) : RentalReviewAppViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        launchCatching {
            val userId = accountService.currentUser
            _uiState.update { it.copy(displayName =  userId.first().name, email = userId.first().email, userId = userId.first().id) }
        }
    }

    fun signOut(navAfterLogOut: () -> Unit){
        launchCatching {
            accountService.logOut()
            navAfterLogOut()
        }
    }

    fun myReviews(){
        launchCatching {
            val reviews = storageService.findMyReviews(uiState.value.userId)
        }
    }
}

data class ProfileUiState(
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
)