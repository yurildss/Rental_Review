package com.example.rentalreview.screen.search

import com.example.rentalreview.model.Review
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val reviewRepository: StorageService,
    private val accountService: AccountService
){

}

data class SearchScreenUiState(
    val userId: String = "",
    val reviews: List<Review?> = mutableListOf()
)