package com.example.rentalreview.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentalreview.common.SnackbarManager
import com.example.rentalreview.common.SnackbarMessage.Companion.toSnackbarMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Base ViewModel for the Rental Review application.
 *
 * Provides common utilities for running coroutines with centralized error handling,
 * showing messages in a snackbar when appropriate.
 */

open class RentalReviewAppViewModel() : ViewModel() {
    /**
     * Launches a coroutine in the ViewModel scope with default exception handling.
     *
     * @param snackbar Whether error messages should be displayed in a snackbar.
     *                 Defaults to `true`.
     * @param block The suspendable code block to execute within the coroutine.
     *
     * When an exception occurs during the execution of [block], it is caught and,
     * if [snackbar] is `true`, a message is shown via [SnackbarManager].
     *
     * Example usage:
     * ```
     * launchCatching {
     *     repository.getData()
     * }
     * ```
     */
    fun launchCatching(
        snackbar: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ) =
        viewModelScope.launch(
            context = CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                    Log.e("Error", throwable.message.toString())
                }
            },
            block = block
        )
}