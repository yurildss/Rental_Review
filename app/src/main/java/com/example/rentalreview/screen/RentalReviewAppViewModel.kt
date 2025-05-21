package com.example.rentalreview.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentalreview.common.SnackbarManager
import com.example.rentalreview.common.SnackbarMessage.Companion.toSnackbarMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class RentalReviewAppViewModel() : ViewModel() {
    fun launchCatching(
        snackbar: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ) =
        viewModelScope.launch(
            context = CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                    Log.d("TAG", "launchCatching: $throwable")
                }
            },
            block = block
        )
}