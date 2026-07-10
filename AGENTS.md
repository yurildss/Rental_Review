# AGENTS.md - RentalReview AI Agent Guide

## Project Overview
**RentalReview** is an Android Jetpack Compose app for rating rental properties by location. Uses Firebase (Auth + Firestore), Retrofit for external APIs, Hilt for DI, and StateFlow for state management.

## Architecture Essentials

### Layered Service Architecture
Services are **interfaces** (in `service/`) with **concrete implementations** in `service/impl/`:
- `StorageService` - Firestore database operations (reviews, likes, comments)
- `AccountService` - Firebase Auth user management
- `UploadImageService` - Cloudinary image uploads

Example: When adding a review, `FeedScreenViewModel` calls `storageService.saveReview()` which is implemented by `StorageServiceImpl` using Firestore directly.

### Dependency Injection with Hilt
- **Entry points**: `@AndroidEntryPoint` on `MainActivity`, `@HiltAndroidApp` on `RentalReviewApplication`
- **ViewModels**: Use `@HiltViewModel` + constructor injection for services
- **Singleton services**: Defined in `service/module/FirebaseModule.kt` with `@Module @InstallIn(SingletonComponent::class)`

When adding services to ViewModels, use constructor injection not property injection.

### UI State Pattern
All screens follow this pattern:
```kotlin
data class FeedScreenUiState(
    val userId: String = "",
    val reviews: MutableList<ReviewUiState> = mutableListOf(),
    val comment: String = "",
    // ...
)
val _uiState = MutableStateFlow(FeedScreenUiState())
val uiState = _uiState.asStateFlow()
```
**Key principle**: UI state is mutable internally (`_uiState`), exposed as immutable flow (`uiState`). Use `.update { it.copy(...) }` to modify state safely.

### Screen Organization
- Features live in `screen/{feature}/` (e.g., `screen/home/`, `screen/login/`)
- Each feature may have a `{Feature}Screen.kt` (composable) and `{Feature}ViewModel.kt`
- Navigation defined in `screen/RentalReviewApp.kt` using Jetpack Navigation Compose
- Route-based navigation: `Screens.FEED_SCREEN.name` for string routes

## Critical Developer Workflows

### Build & Run
```bash
# Sync Gradle dependencies (required after dependency changes)
./gradlew build

# Run app in emulator/device
# Use Android Studio "Run" button or: ./gradlew installDebug
```

### Testing
- **Unit tests** in `app/src/test/` use JUnit4 + Mockito (not Hilt)
- **UI tests** in `app/src/androidTest/` use Hilt with `HiltTestRunner`
- **Test dispatcher setup**: Use `StandardTestDispatcher()` for coroutine tests

Example unit test pattern (see `FeedScreenViewModelUnitTest.kt`):
```kotlin
val storageService = mock<StorageService>()
val accountService = mock<AccountService>()
whenever(storageService.getReviews()).thenReturn(mockData)
viewModel = FeedScreenViewModel(storageService, accountService)
```

## Project Conventions & Patterns

### State Updates & Coroutines
- Always use `withContext(Dispatchers.IO)` for async service calls in ViewModels
- Use `launchCatching` (custom extension in base ViewModel) for error handling in UI coroutines
- Firestore operations: use `await()` to convert Task to suspend (e.g., `firestore.collection(...).get().await()`)
- List updates in Firebase use `FieldValue.arrayUnion()` and `FieldValue.arrayRemove()`

### Mappers & Helpers
- `helper/ReviewMapper.kt` and `AddressMapper.kt` convert between domain and Firestore models
- Extensions like `toReviewUiState()` live in ViewModels (see line 224 in `FeedScreenViewModel.kt`)
- API models from `network/GeoApiService.kt` use Retrofit suspend functions

### Error Patterns
- No explicit error states visible in UI (errors logged via `launchCatching`)
- SnackBar notifications via `SnackBarManager` for user feedback

## External Integrations

### Firebase
- **Firestore collections**: `REVIEWS`, `USERS` (constants in impl files)
- **Document fields**: `title`, `rating`, `review`, `timestamp`, `likesIds`, `comments`, `imageUri`
- **Batch operations**: Use batch writers for multi-document updates per best practices

### APIs
- **CountryStateCity API**: `network/GeoApiService.kt` fetches countries → states → cities
- **Header injection**: OkHttp interceptor adds `X-CSCAPI-KEY` header automatically
- **Base URL**: `https://api.countrystatecity.in/v1/`

### Images
- **Cloudinary** (initialized in `RentalReviewApplication.kt` with `cloud_name = "dgfwjjbx8"`)
- **Coil** for Compose image loading

## Key Files Reference
- `Screens.kt` - Navigation enum (add screens here, not elsewhere)
- `gradle/libs.versions.toml` - Centralized dependency versions
- `app/build.gradle.kts` - Hilt plugin configuration, Firebase BoM setup
- `service/module/ServiceModule.kt` - Implementation binding for interfaces (@Binds pattern expected)

## Build Configuration
- **Target SDK**: 35 (Android 15)
- **Min SDK**: 24 (Android 7)
- **Java/Kotlin**: Version 11 JVM target
- **Kotlin compiler**: 2.0.0 with Compose support enabled
- **Gradle**: 8.9.2, uses version catalog pattern

---
*Keep service implementations focused, use suspend functions throughout async layers, and leverage Compose state management for UI updates.*

