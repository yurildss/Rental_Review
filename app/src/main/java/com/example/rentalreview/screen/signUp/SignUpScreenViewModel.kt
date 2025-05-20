package com.example.rentalreview.screen.signUp

import androidx.lifecycle.ViewModel
import com.example.rentalreview.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel
@Inject constructor(
    private val accountService: AccountService
) : ViewModel()
{

}