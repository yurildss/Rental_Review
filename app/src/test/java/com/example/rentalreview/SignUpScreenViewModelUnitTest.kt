package com.example.rentalreview

import com.example.rentalreview.screen.signUp.SignUpScreenViewModel
import com.example.rentalreview.service.AccountService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class SignUpScreenViewModelUnitTest {
    private lateinit var viewModel: SignUpScreenViewModel
    private lateinit var accountService: AccountService
    private lateinit var testDispatcher: TestDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before fun setup(){
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        accountService = mock<AccountService>()

        runTest {
            whenever(accountService.register(any(), any(), any())).thenReturn(Unit)
        }

        viewModel = SignUpScreenViewModel(accountService)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }

    @Test fun `on name change`(){
        viewModel.onNameChange("Unit Test")
        assert(viewModel.uiState.value.name == "Unit Test")
    }

    @Test fun `on email change`(){
        viewModel.onEmailChange("Unit Test")
        assert(viewModel.uiState.value.email == "Unit Test")
    }

    @Test fun `on password change`(){
        viewModel.onPasswordChange("Unit Test")
        assert(viewModel.uiState.value.password == "Unit Test")
    }

    @Test fun `on repeat password change`(){
        viewModel.onRepeatPasswordChange("Unit Test")
        viewModel.onRepeatPasswordChange("Unit Test")
        assert(viewModel.uiState.value.repeatPassword == viewModel.uiState.value.repeatPassword)
    }

    @Test fun `don't register without name`() = runTest{
        var wasCalled = true

        viewModel.onNameChange("")
        viewModel.onEmailChange("Unit Test")
        viewModel.onPasswordChange("Unit Test")
        viewModel.onRepeatPasswordChange("Unit Test")
        viewModel.onSignUpClick {
            wasCalled = false
        }

        assert(wasCalled)

    }

    @Test fun `don't register without email`() = runTest{
        var wasCalled = true

        viewModel.onNameChange("Unit Test")
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("Unit Test")
        viewModel.onRepeatPasswordChange("Unit Test")
        viewModel.onSignUpClick {
            wasCalled = false
        }
        assert(wasCalled)
    }

    @Test fun `don't register without password`() = runTest{
        var wasCalled = true

        viewModel.onNameChange("Unit Test")
        viewModel.onEmailChange("Unit Test")
        viewModel.onPasswordChange("")
        viewModel.onRepeatPasswordChange("Unit Test")
        viewModel.onSignUpClick {
            wasCalled = false
        }
        assert(wasCalled)
    }

    @Test fun `don't register without a valid email`() = runTest{
        var wasCalled = true

        viewModel.onNameChange("Unit Test")
        viewModel.onEmailChange("Unit Test")
        viewModel.onPasswordChange("Unit Test")
        viewModel.onRepeatPasswordChange("Unit Test")
        viewModel.onSignUpClick {
            wasCalled = false
        }

        assert(wasCalled)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun `register with valid inputs`() = runTest{
        var wasCalled = false

        viewModel.onNameChange("Unit Test")
        viewModel.onEmailChange("test@test.com")
        viewModel.onPasswordChange("88295886yY")
        viewModel.onRepeatPasswordChange("88295886yY")

        viewModel.onSignUpClick {
            wasCalled = true
        }

        advanceUntilIdle()
        assert(wasCalled)
    }

}