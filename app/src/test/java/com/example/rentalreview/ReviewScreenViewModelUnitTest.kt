package com.example.rentalreview

import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.State
import kotlinx.coroutines.test.resetMain

import com.example.rentalreview.screen.review.ReviewScreenViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.ZoneId

class ReviewScreenViewModelUnitTest {
    private lateinit var viewModel: ReviewScreenViewModel
    private lateinit var reviewRepository: StorageService
    private lateinit var accountService: AccountService
    private lateinit var testDispatcher: TestDispatcher
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){

        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        runTest {
            whenever(reviewRepository.saveReview(any())).thenReturn(Unit)
        }

        reviewRepository = mock<StorageService>()
        accountService = mock<AccountService>()
        viewModel = ReviewScreenViewModel(reviewRepository, accountService)


    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `on rating change`(){
        viewModel.onRatingChanged(5)
        assert(viewModel.uiState.value.rating == 5)

    }

    @Test
    fun `on country changed`(){
        viewModel.onCountryChanged(Country("Unit Test", "Unit Test", "Unit Test"))
        assert(viewModel.uiState.value.selectedCountryItem == Country("Unit Test", "Unit Test", "Unit Test"))

    }

    @Test
    fun `on Zip Changed`(){
        viewModel.onZipChanged("Unit Test")
        assert(viewModel.uiState.value.zip == "Unit Test")
    }

    @Test
    fun `on State Changed`(){
        viewModel.onStateSelected(State("Unit Test", "Unit Test", "Unit Test"))
        assert(viewModel.uiState.value.selectedStateItem == State("Unit Test", "Unit Test", "Unit Test"))
    }

    @Test
    fun `on City Changed`(){
        viewModel.onCitySelected(City(0, "Unit Test"))
    }

    @Test
    fun `on Number Changed`(){
        viewModel.onNumberChanged("Unit Test")
        assert(viewModel.uiState.value.number == "Unit Test")
    }

    @Test
    fun `on Street Changed`(){
        viewModel.onStreetChanged("Unit Test")
        assert(viewModel.uiState.value.street == "Unit Test")
    }

    @Test
    fun `on Title Changed`(){
        viewModel.onTitleChanged("Unit Test")
        assert(viewModel.uiState.value.title == "Unit Test")
    }

    @Test
    fun `on Type Changed`(){
        viewModel.typeRental("Unit Test")
        assert(viewModel.uiState.value.type == "Unit Test")
    }

    @Test
    fun `on Review Changed`(){
        viewModel.updateReview("Unit Test")
        assert(viewModel.uiState.value.review == "Unit Test")
    }

    @Test
    fun `on Expanded Options Changed`(){
        viewModel.updateExpandedOptions(true)
        assert(viewModel.uiState.value.expandedOptions)
    }


    @Test
    fun `on Date Range Selected`() {

        val startMillis = LocalDate.of(2025, 7, 26)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()


        val endMillis = LocalDate.of(2025, 7, 27)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        viewModel.onDateRangeSelected(startMillis, endMillis)

        assert(LocalDate.of(2025, 7, 26)==viewModel.startDate.value)
        assert(LocalDate.of(2025, 7, 27)==viewModel.endDate.value)
    }
}