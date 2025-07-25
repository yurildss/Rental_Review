package com.example.rentalreview

import com.example.rentalreview.screen.login.LoginScreenViewModel
import com.example.rentalreview.service.AccountService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class LoginScreenViewModelUnitTest {

    private lateinit var viewModel: LoginScreenViewModel
    private lateinit var accountService: AccountService

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        accountService = mock<AccountService>()

        runTest {
            whenever(accountService.authenticate(any(),any())).thenReturn(Unit)
        }

        viewModel = LoginScreenViewModel(accountService)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test fun onEmailChange() {
        viewModel.onEmailChange("Unit Test")
        assert(viewModel.uiState.value.email == "Unit Test")
    }

    @Test fun onPasswordChange() {
        viewModel.onPasswordChange("Unit Test")
        assert(viewModel.uiState.value.password == "Unit Test")
    }

    @Test fun `don't login without email`() {
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("Unit Test")
        viewModel.onLoginClick({
            assert(false)
        })
        assert(true)
    }

    @Test fun `don't login without password`() {
        viewModel.onEmailChange("Unit Test")
        viewModel.onPasswordChange("")
        viewModel.onLoginClick({
            assert(false)
        })
        assert(true)
    }

    @Test fun `login with email and password`() {
        viewModel.onEmailChange("Unit Test")
        viewModel.onPasswordChange("Unit Test")
        viewModel.onLoginClick({
            assert(true)
        }
        )
    }
}