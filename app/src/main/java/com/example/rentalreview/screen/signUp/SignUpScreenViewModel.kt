package com.example.rentalreview.screen.signUp

import android.util.Log
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
        onSignUpSuccess: () -> Unit
    ) {
        if (uiState.value.name.isBlank()){
            Log.d("SignUpScreenViewModel", "onSignUpClick:1")
            SnackbarManager.showMessage(R.string.name_error)
            return
        }

        if (!uiState.value.email.isValidEmail()){
            Log.d("SignUpScreenViewModel", "onSignUpClick:2")
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (!uiState.value.password.isValidPassword()){
            Log.d("SignUpScreenViewModel", "onSignUpClick:3")
            SnackbarManager.showMessage(R.string.password_error)
            return
        }

        if(uiState.value.password != uiState.value.repeatPassword){
            Log.d("SignUpScreenViewModel", "onSignUpClick:4")
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        launchCatching {
            accountService.register(
                email = uiState.value.email,
                password =uiState.value.password,
                name = uiState.value.name,
            )
            Log.d("SignUpScreenViewModel", "onSignUpClick:5")
            SnackbarManager.showMessage(R.string.sign_up_success)
            onSignUpSuccess()
        }
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
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