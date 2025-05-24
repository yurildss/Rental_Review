package com.example.rentalreview

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.collections.isNotEmpty

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
        composeTestRule.onNodeWithTag("emailTextField").performTextInput("william.henry.harrison@hotmail.com")
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

        composeTestRule.onNodeWithTag("emailTextField").performTextInput("william.henry.harrison@hotmail.com")
        composeTestRule.onNodeWithTag("passwordTextField").performTextInput("password123yY")

        composeTestRule.onNodeWithTag("loginButton").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000){
            composeTestRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
