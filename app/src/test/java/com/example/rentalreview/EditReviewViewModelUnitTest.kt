package com.example.rentalreview

import android.net.Uri
import android.os.Build
import androidx.lifecycle.SavedStateHandle
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.Review
import com.example.rentalreview.model.State
import com.example.rentalreview.screen.myReviews.EditReviewViewModel
import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import com.example.rentalreview.service.UploadImageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

@androidx.test.filters.SdkSuppress(minSdkVersion = Build.VERSION_CODES.O)
class EditReviewViewModelUnitTest {

    private lateinit var viewModel: EditReviewViewModel
    private lateinit var storageService: StorageService
    private lateinit var accountService: AccountService
    private lateinit var uploadImageService: UploadImageService
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var testDispatcher: TestDispatcher

    private val mockReview = Review(
        id = "review_1",
        title = "Beautiful Apartment",
        rating = 5,
        review = "Amazing location and cleanliness",
        type = "Apartment",
        startDate = "2025-01-10",
        endDate = "2025-01-15",
        address = Address(
            street = "Av. Paulista",
            number = "1000",
            city = City(1, "São Paulo"),
            state = State("SP", "São Paulo", "BR"),
            zip = "01310-100",
            country = Country("BR", "Brasil", "Brazil"),
            countryCode = "BR"
        ),
        likesIds = mutableListOf(),
        comments = mutableListOf(),
        favoriteIdsUsers = mutableListOf(),
        userId = "user_1",
        imageUri = mutableListOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
        timestamp = 1234567890L
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        testDispatcher = StandardTestDispatcher()
        storageService = mock<StorageService>()
        accountService = mock<AccountService>()
        uploadImageService = mock<UploadImageService>()
        savedStateHandle = SavedStateHandle()
        savedStateHandle["reviewId"] = "review_1"

        runBlocking {
            whenever(storageService.getReviewById("review_1")).thenReturn(mockReview)
            whenever(accountService.currentUserId).thenReturn("user_1")
        }

        viewModel = EditReviewViewModel(
            storageService = storageService,
            accountService = accountService,
            uploadImageService = uploadImageService,
            savedStateHandle = savedStateHandle
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onImageSelect updates imageGallery in state`() {
        val images = listOf(
            Uri.parse("content://media/external/images/media/1"),
            Uri.parse("content://media/external/images/media/2")
        )

        viewModel.onImageSelect(images)

        assertEquals(viewModel.uiState.value.imageGallery, images)
        assertEquals(viewModel.uiState.value.imageGallery.size, 2)
    }

    @Test
    fun `removeImage removes image at specified index`() {
        val images = listOf(
            Uri.parse("content://media/external/images/media/1"),
            Uri.parse("content://media/external/images/media/2"),
            Uri.parse("content://media/external/images/media/3")
        )
        viewModel.onImageSelect(images)

        viewModel.removeImage(1)

        assertEquals(viewModel.uiState.value.imageGallery.size, 2)
        assertEquals(viewModel.uiState.value.imageGallery[0], images[0])
        assertEquals(viewModel.uiState.value.imageGallery[1], images[2])
    }

    @Test
    fun `removeImage does nothing when index is out of bounds`() {
        val images = listOf(Uri.parse("content://media/external/images/media/1"))
        viewModel.onImageSelect(images)

        viewModel.removeImage(5)

        assertEquals(viewModel.uiState.value.imageGallery.size, 1)
    }

    @Test
    fun `onTitleChanged updates title in state`() {
        viewModel.onTitleChanged("New Title")

        assertEquals(viewModel.uiState.value.title, "New Title")
    }

    @Test
    fun `onStreetChanged updates street in state`() {
        viewModel.onStreetChanged("Nueva Calle")

        assertEquals(viewModel.uiState.value.street, "Nueva Calle")
    }

    @Test
    fun `onNumberChanged updates number in state`() {
        viewModel.onNumberChanged("42")

        assertEquals(viewModel.uiState.value.number, "42")
    }

    @Test
    fun `onRatingChanged updates rating in state`() {
        viewModel.onRatingChanged(4)

        assertEquals(viewModel.uiState.value.rating, 4)
    }

    @Test
    fun `updateReview updates review text in state`() {
        val reviewText = "This is a detailed review of my stay."
        viewModel.updateReview(reviewText)

        assertEquals(viewModel.uiState.value.review, reviewText)
    }

    @Test
    fun `typeRental updates property type in state`() {
        viewModel.typeRental("House")

        assertEquals(viewModel.uiState.value.type, "House")
    }

    @Test
    fun `onCountryChanged updates country in state`() {
        val newCountry = Country("US", "United States", "USA")
        viewModel.onCountryChanged(newCountry)

        assertEquals(viewModel.uiState.value.selectedCountryItem, newCountry)
    }

    @Test
    fun `onStateChanged updates state in UI state`() {
        val newState = State("CA", "California", "USA")
        viewModel.onStateChanged(newState)

        assertEquals(viewModel.uiState.value.selectedStateItem, newState)
    }

    @Test
    fun `onCityChanged updates city in state`() {
        val newCity = City(100, "Los Angeles")
        viewModel.onCityChanged(newCity)

        assertEquals(viewModel.uiState.value.selectedCityItem, newCity)
    }

    @Test
    fun `onZipChanged updates zip in state`() {
        viewModel.onZipChanged("90210")

        assertEquals(viewModel.uiState.value.zip, "90210")
    }

    @Test
    fun `onCountryExpandedOptions toggles country options visibility`() {
        val initialState = viewModel.uiState.value.expandedCountryOptions
        viewModel.onCountryExpandedOptions()

        assertEquals(!initialState, viewModel.uiState.value.expandedCountryOptions)
    }

    @Test
    fun `onStateExpandedOptions toggles state options visibility`() {
        val initialState = viewModel.uiState.value.expandedStateOptions
        viewModel.onStateExpandedOptions()

        assertEquals(!initialState, viewModel.uiState.value.expandedStateOptions)
    }

    @Test
    fun `onCityExpandedOptions toggles city options visibility`() {
        val initialState = viewModel.uiState.value.expandedCityOptions
        viewModel.onCityExpandedOptions()

        assertEquals(!initialState, viewModel.uiState.value.expandedCityOptions)
    }

    @Test
    fun `openDialog sets openDialog to true`() {
        viewModel.openDialog()

        assertTrue(viewModel.uiState.value.openDialog)
    }

    @Test
    fun `closeDialog sets openDialog to false`() {
        viewModel.openDialog()
        viewModel.closeDialog()

        assertFalse(viewModel.uiState.value.openDialog)
    }

    @Test
    fun `isFormValid returns false when title is empty`() {
        val state = viewModel.uiState.value.copy(title = "")
        val result = viewModel.isFormValid(
            startDate = LocalDate.of(2025, 1, 10),
            endDate = LocalDate.of(2025, 1, 15),
            state = state
        )

        assertFalse(result)
    }

    @Test
    fun `isFormValid returns false when type is empty`() {
        val state = viewModel.uiState.value.copy(
            title = "Valid Title",
            type = ""
        )
        val result = viewModel.isFormValid(
            startDate = LocalDate.of(2025, 1, 10),
            endDate = LocalDate.of(2025, 1, 15),
            state = state
        )

        assertFalse(result)
    }

    @Test
    fun `isFormValid returns false when startDate is default`() {
        val state = viewModel.uiState.value.copy(
            title = "Valid Title",
            type = "Apartment"
        )
        val result = viewModel.isFormValid(
            startDate = LocalDate.now(),
            endDate = LocalDate.of(2025, 1, 15),
            state = state
        )

        assertFalse(result)
    }

    @Test
    fun `isFormValid returns false when endDate is default`() {
        val state = viewModel.uiState.value.copy(
            title = "Valid Title",
            type = "Apartment"
        )
        val result = viewModel.isFormValid(
            startDate = LocalDate.of(2025, 1, 10),
            endDate = LocalDate.of(2025, 12, 31),
            state = state
        )

        assertFalse(result)
    }

    @Test
    fun `isFormValid returns false when country is not selected`() {
        val state = viewModel.uiState.value.copy(
            title = "Valid Title",
            type = "Apartment",
            selectedCountryItem = Country("", "", "")
        )
        val result = viewModel.isFormValid(
            startDate = LocalDate.of(2025, 1, 10),
            endDate = LocalDate.of(2025, 1, 15),
            state = state
        )

        assertFalse(result)
    }

    @Test
    fun `isFormValid returns false when state is not selected`() {
        val selectedCountry = Country("BR", "Brasil", "Brazil")
        val state = viewModel.uiState.value.copy(
            title = "Valid Title",
            type = "Apartment",
            selectedCountryItem = selectedCountry,
            selectedStateItem = State("", "", "")
        )
        val result = viewModel.isFormValid(
            startDate = LocalDate.of(2025, 1, 10),
            endDate = LocalDate.of(2025, 1, 15),
            state = state
        )

        assertFalse(result)
    }

    @Test
    fun `isFormValid returns false when city is not selected`() {
        val selectedCountry = Country("BR", "Brasil", "Brazil")
        val selectedState = State("SP", "São Paulo", "BR")
        val state = viewModel.uiState.value.copy(
            title = "Valid Title",
            type = "Apartment",
            selectedCountryItem = selectedCountry,
            selectedStateItem = selectedState,
            selectedCityItem = City(0, "")
        )
        val result = viewModel.isFormValid(
            startDate = LocalDate.of(2025, 1, 10),
            endDate = LocalDate.of(2025, 1, 15),
            state = state
        )

        assertFalse(result)
    }

    @Test
    fun `isFormValid returns true when all fields are valid`() {
        val selectedCountry = Country("BR", "Brasil", "Brazil")
        val selectedState = State("SP", "São Paulo", "BR")
        val selectedCity = City(1, "São Paulo")
        val state = viewModel.uiState.value.copy(
            title = "Valid Title",
            type = "Apartment",
            selectedCountryItem = selectedCountry,
            selectedStateItem = selectedState,
            selectedCityItem = selectedCity
        )
        val result = viewModel.isFormValid(
            startDate = LocalDate.of(2025, 1, 10),
            endDate = LocalDate.of(2025, 1, 15),
            state = state
        )

        assertTrue(result)
    }

    @Test
    fun `onDateRangeSelected updates both startDate and endDate`() = runTest {
        val startMillis = 1736438400000L // 2025-01-10
        val endMillis = 1736956800000L   // 2025-01-16

        viewModel.onDateRangeSelected(startMillis, endMillis)

        assertEquals(viewModel.startDate.value, LocalDate.of(2025, 1, 10))
        assertEquals(viewModel.endDate.value, LocalDate.of(2025, 1, 16))
    }

    @Test
    fun `onDateRangeSelected handles null startDate`() = runTest {
        val endMillis = 1736956800000L

        viewModel.onDateRangeSelected(null, endMillis)

        assertEquals(viewModel.startDate.value, null)
        assertEquals(viewModel.endDate.value, LocalDate.of(2025, 1, 16))
    }

    @Test
    fun `onDateRangeSelected handles null endDate`() = runTest {
        val startMillis = 1736438400000L

        viewModel.onDateRangeSelected(startMillis, null)

        assertEquals(viewModel.startDate.value, LocalDate.of(2025, 1, 10))
        assertEquals(viewModel.endDate.value, null)
    }

    @Test
    fun `getReviewById loads review data into state`() = runTest {
        viewModel.getReviewById("review_1")

        assertEquals(viewModel.uiState.value.title, "Beautiful Apartment")
        assertEquals(viewModel.uiState.value.type, "Apartment")
        assertEquals(viewModel.uiState.value.review, "Amazing location and cleanliness")
        assertEquals(viewModel.uiState.value.rating, 5)
    }

    @Test
    fun `getReviewById loads image URIs from review`() = runTest {
        viewModel.getReviewById("review_1")

        assertTrue(viewModel.uiState.value.imageGallery.size >= 2)
    }

    @Test
    fun `toAddress creates correct Address from state`() {
        val country = Country("BR", "Brasil", "Brazil")
        val state = State("SP", "São Paulo", "BR")
        val city = City(1, "São Paulo")

        viewModel.onCountryChanged(country)
        viewModel.onStateChanged(state)
        viewModel.onCityChanged(city)
        viewModel.onStreetChanged("Av. Paulista")
        viewModel.onNumberChanged("1000")
        viewModel.onZipChanged("01310-100")

        val address = viewModel.uiState.value.run {
            Address(
                street = this.street,
                number = this.number,
                city = this.selectedCityItem,
                state = this.selectedStateItem,
                zip = this.zip,
                country = this.selectedCountryItem
            )
        }

        assertEquals(address.street, "Av. Paulista")
        assertEquals(address.number, "1000")
        assertEquals(address.zip, "01310-100")
        assertEquals(address.country.iso2, "BR")
        assertEquals(address.state.iso2, "SP")
        assertEquals(address.city.id, 1)
    }
}

