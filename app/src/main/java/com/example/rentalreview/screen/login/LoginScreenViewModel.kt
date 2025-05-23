package com.example.rentalreview.screen.login

import com.example.rentalreview.R
import com.example.rentalreview.common.SnackbarManager
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel
@Inject constructor(
    private val accountService: AccountService
) : RentalReviewAppViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String){
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String){
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onLoginClick(onLoginSuccess: () -> Unit){
        if (uiState.value.email.isBlank()){
            SnackbarManager.showMessage(R.string.email_error)
        }

        if (uiState.value.password.isBlank()){
            SnackbarManager.showMessage(R.string.password_error)
        }


        launchCatching {
            accountService.authenticate(uiState.value.email, uiState.value.password)
            onLoginSuccess()
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
)