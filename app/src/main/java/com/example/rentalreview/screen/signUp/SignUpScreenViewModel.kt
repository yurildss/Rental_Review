package com.example.rentalreview.screen.signUp

import androidx.lifecycle.ViewModel
import com.example.rentalreview.R
import com.example.rentalreview.common.SnackbarManager
import com.example.rentalreview.common.isValidEmail
import com.example.rentalreview.common.isValidPassword
import com.example.rentalreview.screen.RentalReviewAppViewModel
import com.example.rentalreview.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel
@Inject constructor(
    private val accountService: AccountService
) : RentalReviewAppViewModel(){

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onSignUpClick(
        name: String,
        email: String,
        password: String,
        repeatPassword: String,
        onSignUpSuccess: () -> Unit
    ) {
        if (name.isBlank()){
            SnackbarManager.showMessage(R.string.name_error)
            return
        }

        if (!email.isValidEmail()){
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (!password.isValidPassword()){
            SnackbarManager.showMessage(R.string.password_error)
            return
        }

        if(password != repeatPassword){
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        launchCatching {
            accountService.register(email, password, name)
            SnackbarManager.showMessage(R.string.sign_up_success)
            onSignUpSuccess()
        }
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onRepeatPasswordChange(repeatPassword: String) {
        _uiState.value = _uiState.value.copy(repeatPassword = repeatPassword)
    }
}

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
)