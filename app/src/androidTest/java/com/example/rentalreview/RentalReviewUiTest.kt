package com.example.rentalreview

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import kotlin.collections.isNotEmpty
import kotlin.random.Random

@HiltAndroidTest
class RentalReviewUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun register() {
        composeTestRule.onNodeWithTag("getStartedButton").performClick()
        composeTestRule.onNodeWithTag("loginScreen").assertExists()

        composeTestRule.onNodeWithTag("signUpButton").performClick()
        composeTestRule.onNodeWithTag("signUpScreen").assertExists()

        composeTestRule.onNodeWithTag("nameTextField").performTextInput("Test User")
        composeTestRule.onNodeWithTag("emailTextField").performTextInput("willi${Random.nextInt(0,100)}@hotmail.com")
        composeTestRule.onNodeWithTag("passwordTextField").performTextInput("password123yY")
        composeTestRule.onNodeWithTag("repeatPasswordTextField").performTextInput("password123yY")
        composeTestRule.onNodeWithTag("signUpButton").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000){
            composeTestRule.onAllNodesWithTag("loginScreen").fetchSemanticsNodes().isNotEmpty()
        }

    }

    @Test
    fun login() {
        composeTestRule.onNodeWithTag("getStartedButton").performClick()
        composeTestRule.onNodeWithTag("loginScreen").assertExists()

        composeTestRule.onNodeWithTag("emailTextField").performTextInput("williamhenryharrison@hotmail.com")
        composeTestRule.onNodeWithTag("passwordTextField").performTextInput("password123yY")

        composeTestRule.onNodeWithTag("loginButton").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000){
            composeTestRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun review() {
        login()

        Thread.sleep(2000L)
        //navigate to add screen
        composeTestRule.onNodeWithTag("addScreen").assertExists().performClick()
        //wait for screen to load
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("reviewEntryScreen").fetchSemanticsNodes().isNotEmpty()
        }
        //write a tittle review
        composeTestRule.onNodeWithTag("titleEntry").performTextInput("Test Title ${Random.nextInt(0,100)}")
        //choose a country
        composeTestRule.onNodeWithTag("Country").performClick()
        composeTestRule.waitUntil(timeoutMillis = 20000){
            composeTestRule.onAllNodesWithTag("Argentina").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("Argentina").performClick()
        //choose a street and number
        composeTestRule.onNodeWithTag("streetEntry").performTextInput("Test Street ${Random.nextInt(0,100)}")
        composeTestRule.onNodeWithTag("numberEntry").performTextInput("123")

        composeTestRule.onNodeWithTag("propertyType").performClick()
        composeTestRule.waitUntil(timeoutMillis = 10000){
            composeTestRule.onAllNodesWithTag("Apartment").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("Apartment").performClick()

        composeTestRule.onNodeWithTag("dateRangeSelector").performClick()
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText("OK").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("OK").performClick()

        composeTestRule.onNodeWithTag("reviewEntry").performTextInput("Test Review ${Random.nextInt(0,100)}")

        composeTestRule.onNodeWithTag("saveReview").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000){
            composeTestRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }
    }


    @Test
    fun navigateToFeedScreen(){
        login()
        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule.onAllNodesWithTag("reviewCard").fetchSemanticsNodes().isNotEmpty()
        }

    }

    @Test
    fun myReviews(){

        login()
        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule.onAllNodesWithTag("reviewCard").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("accountScreen").performClick()
        composeTestRule.onNodeWithTag("myReviewsTag").performClick()
        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule.onAllNodesWithTag("reviewCard").fetchSemanticsNodes().isNotEmpty()
        }
    }

}
