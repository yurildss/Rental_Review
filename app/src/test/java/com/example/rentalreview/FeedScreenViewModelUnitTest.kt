package com.example.rentalreview

import com.example.rentalreview.model.Address
import com.example.rentalreview.model.Review
import com.example.rentalreview.screen.home.FeedScreenViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class FeedScreenViewModelUnitTest {

    private lateinit var viewModel: FeedScreenViewModel
    private lateinit var storageService: StorageService
    private lateinit var accountService: AccountService
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var listOfReviews: List<Review>
    @Before
    fun setup(){

        listOfReviews = listOf(
            Review(
                id = "1",
                title = "test",
                rating = 5,
                review = "test",
                type = "Apartment",
                startDate = "2025-07-22",
                endDate = "2025-08-25",
                userId = "1",
                address = Address(
                    country = "test country",
                    city = "test city",
                    street = "test street",
                    number = "10",
                    zip = "12345")
            ),
            Review(
                id = "2",
                title = "test2",
                rating = 4,
                review = "test2",
                type = "Apartment",
                startDate = "2025-08-22",
                endDate = "2025-09-25",
                userId = "1",
                address = Address(
                    country = "test country2",
                    city = "test city2",
                    street = "test street2",
                    number = "9",
                    zip = "12375")
            )
        )

        testDispatcher = StandardTestDispatcher()
        storageService = mock<StorageService>()
        accountService = mock<AccountService>()
        runTest {
            whenever(accountService.currentUserId).thenReturn("1")

            whenever(storageService.getReviews()).thenReturn(listOfReviews)

            whenever(storageService.comment("1", "1", "test comment")).thenReturn(Unit)

            whenever(storageService.updateLikes("1", "1")).thenReturn(Unit)

            whenever(storageService.addFavorite("1", "1")).thenReturn(Unit)

            whenever(storageService.removeLike("1", "1")).thenReturn(Unit)
        }

        viewModel = FeedScreenViewModel(storageService, accountService)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `Get initial reviews`() = runTest {
        viewModel.getInitialReviews()

        assert(viewModel.uiState.value.reviews.size == 2)
        assert(viewModel.uiState.value.reviews[0]?.title == "test")
        assert(viewModel.uiState.value.reviews[1]?.title == "test2")
        assert(viewModel.uiState.value.userId == "1")
    }

    @Test
    fun `add comments`() = runTest {
        viewModel.getInitialReviews()
        viewModel.onCommentChange("test comment")

        viewModel.addNewComment("1", 0)
        assert(viewModel.uiState.value.reviews[0]?.comments?.size == 1)
    }

    @Test
    fun `on show comment click`() = runTest {
        viewModel.getInitialReviews()
        viewModel.onShowCommentClick(0)
        assert(viewModel.uiState.value.reviews[0]?.showComment == true)

    }

    @Test
    fun `like a review`() = runTest {
        viewModel.getInitialReviews()
        viewModel.likeReview("1", 0)
        assert(viewModel.uiState.value.reviews[0]?.likesIds?.size == 1)

    }

    @Test
    fun `add reviews to favorites`() = runTest {
        viewModel.getInitialReviews()
        viewModel.addFavorite("1", 0)
        assert(viewModel.uiState.value.reviews[0]?.favoriteIds?.size == 1)

    }

    @Test
    fun `remover a like from a review`() = runTest {
        viewModel.getInitialReviews()
        viewModel.likeReview("1", 0)
        viewModel.unlikeReview("1", 0)
        assert(viewModel.uiState.value.reviews[0]?.likesIds?.size == 0)

    }
}