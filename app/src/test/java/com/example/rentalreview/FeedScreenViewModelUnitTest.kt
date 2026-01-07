package com.example.rentalreview

import com.example.rentalreview.model.Address
import com.example.rentalreview.model.City
import com.example.rentalreview.model.Comments
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.Review
import com.example.rentalreview.model.State
import com.example.rentalreview.screen.home.FeedScreenViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
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
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){

        listOfReviews = listOf(
            Review(
                id = "review_1",
                title = "Ótimo apartamento",
                rating = 5,
                review = "Lugar limpo, bem localizado e confortável.",
                type = "Apartamento",
                startDate = "2025-01-10",
                endDate = "2025-01-15",
                address = Address(
                    street = "Av. Paulista",
                    number = "1000",
                    city = City(1, "São Paulo"),
                    state = State("SP", "São Paulo", "Brasil"),
                    zip = "01310-100",
                    country = Country("BR", "Brasil", "Brazil"),
                    countryCode = "BR"
                ),
                likesIds = mutableListOf("user_2", "user_3"),
                comments = mutableListOf(
                    Comments(
                        userId = "user_4",
                        comment = "Também gostei bastante!"
                    )
                ),
                favoriteIdsUsers = mutableListOf("user_5"),
                userId = "user_1"
            ),

            Review(
                id = "review_2",
                title = "Experiência mediana",
                rating = 3,
                review = "Atendeu o básico, mas poderia melhorar.",
                type = "Casa",
                startDate = "2024-12-01",
                endDate = "2024-12-05",
                address = Address(
                    street = "Rua das Flores",
                    number = "45",
                    city = City(2, "Curitiba"),
                    state = State("PR", "Paraná", "Brasil"),
                    zip = "80000-000",
                    country = Country("BR", "Brasil", "Brazil"),
                    countryCode = "BR"
                ),
                likesIds = mutableListOf(),
                comments = mutableListOf(),
                favoriteIdsUsers = mutableListOf(),
                userId = "user_2"
            ),

            Review(
                id = "review_3",
                title = "Não recomendo",
                rating = 1,
                review = "Problemas sérios de limpeza e barulho.",
                type = "Quarto",
                startDate = "2024-11-20",
                endDate = "2024-11-22",
                address = Address(
                    street = "Calle Florida",
                    number = "200",
                    city = City(3, "Buenos Aires"),
                    state = State("BA", "Buenos Aires", "Argentina"),
                    zip = "C1005",
                    country = Country("AR", "Argentina", "Argentina"),
                    countryCode = "AR"
                ),
                likesIds = mutableListOf("user_1"),
                comments = mutableListOf(
                    Comments(
                        userId = "user_3",
                        comment = "Minha experiência foi diferente."
                    )
                ),
                favoriteIdsUsers = mutableListOf(),
                userId = "user_6"
            )
        )



        testDispatcher = UnconfinedTestDispatcher()
        storageService = mock<StorageService>()
        accountService = mock<AccountService>()

        runBlocking {
            whenever(accountService.currentUserId).thenReturn("user_1")

            whenever(storageService.getReviews()).thenReturn(listOfReviews)

            whenever(storageService.comment("review_1", "user_4", "Também gostei bastante!")).thenReturn(Unit)

            whenever(storageService.comment("review_2", "", "")).thenReturn(Unit)

            whenever(storageService.comment("review_3", "user_3", "Minha experiência foi diferente.")).thenReturn(Unit)

            whenever(storageService.updateLikes("review_1", "user_1")).thenReturn(Unit)

            whenever(storageService.addFavorite("1", "1")).thenReturn(Unit)

            whenever(storageService.removeLike("review_1", "user_1")).thenReturn(Unit)
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

        assert(viewModel.uiState.value.reviews.size == 3)
        assert(viewModel.uiState.value.reviews[0]?.title == "Ótimo apartamento")
        assert(viewModel.uiState.value.reviews[1]?.title == "Experiência mediana")
        assert(viewModel.uiState.value.reviews[1]?.title == "Não recomendo")
        assert(viewModel.uiState.value.userId == "user_1")
    }

    @Test
    fun `add comments`() = runTest {
        viewModel.getInitialReviews()
        viewModel.onCommentChange("test comment")

        viewModel.addNewComment("1", 0)
        assertEquals(viewModel.uiState.value.reviews[0]?.comments?.size, 1)
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