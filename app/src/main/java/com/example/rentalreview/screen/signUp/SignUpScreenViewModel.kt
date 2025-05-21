package com.example.rentalreview.screen.signUp

import androidx.lifecycle.ViewModel
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

    fun onSignUpClick(name: String, email: String, password: String, repeatPassword: String) {

        if (repeatPassword.isEmpty()){
            return
        }

        if (password.isEmpty()){
            return
        }

        if (email.isEmpty()){
            return
        }

        if (name.isEmpty()){
            return
        }

        if(password != repeatPassword){
            return
        }

        launchCatching {
            accountService.register(email, password, name)

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