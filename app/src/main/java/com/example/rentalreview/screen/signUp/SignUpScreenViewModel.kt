package com.example.rentalreview.screen.signUp

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

/**
 * ViewModel for the Sign Up screen.
 *
 * Handles user input, form validation, and account creation by interacting with [AccountService].
 * Inherits coroutine launching and error handling behavior from [RentalReviewAppViewModel].
 *
 * @property accountService Service responsible for account registration and authentication.
 */
@HiltViewModel
class SignUpScreenViewModel
@Inject constructor(
    private val accountService: AccountService
) : RentalReviewAppViewModel(){
    // Internal UI state holder for the Sign Up screen
    private val _uiState = MutableStateFlow(SignUpUiState())
    /**
     * Publicly exposed immutable state for the UI to observe.
     */
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()
    /**
     * Triggered when the user clicks the "Sign Up" button.
     *
     * Performs the following steps:
     * 1. Validates that the name is not blank.
     * 2. Validates the email format.
     * 3. Validates the password requirements.
     * 4. Checks that password and repeat password match.
     * 5. Calls [AccountService.register] to create the account if all validations pass.
     * 6. Displays appropriate success or error messages via [SnackbarManager].
     * 7. Executes [onSignUpSuccess] callback if registration is successful.
     *
     * @param onSignUpSuccess Callback invoked after a successful registration.
     */
    fun onSignUpClick(
        onSignUpSuccess: () -> Unit
    ) {
        if (uiState.value.name.isBlank()){

            SnackbarManager.showMessage(R.string.name_error)
            return
        }

        if (!uiState.value.email.isValidEmail()){

            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (!uiState.value.password.isValidPassword()){

            SnackbarManager.showMessage(R.string.password_error)
            return
        }

        if(uiState.value.password != uiState.value.repeatPassword){

            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        launchCatching {
            accountService.register(
                email = uiState.value.email,
                password =uiState.value.password,
                name = uiState.value.name,
            )

            SnackbarManager.showMessage(R.string.sign_up_success)
            onSignUpSuccess()
        }
    }
    /**
     * Updates the name field in the UI state.
     */
    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }
    /**
     * Updates the email field in the UI state.
     */
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }
    /**
     * Updates the password field in the UI state.
     */
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }
    /**
     * Updates the repeat password field in the UI state.
     */
    fun onRepeatPasswordChange(repeatPassword: String) {
        _uiState.value = _uiState.value.copy(repeatPassword = repeatPassword)
    }
}
/**
 * Holds the UI data for the Sign Up screen.
 *
 * @property name User's full name.
 * @property email User's email address.
 * @property password User's chosen password.
 * @property repeatPassword Confirmation of the user's password.
 */
data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
)