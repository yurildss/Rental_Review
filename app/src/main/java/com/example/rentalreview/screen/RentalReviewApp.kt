package com.example.rentalreview.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rentalreview.Screens
import com.example.rentalreview.screen.favorites.FavoritesScreen
import com.example.rentalreview.screen.home.FeedScreen
import com.example.rentalreview.screen.login.LoginScreen
import com.example.rentalreview.screen.myReviews.EditReviewViewModel
import com.example.rentalreview.screen.myReviews.MyReviewsScreen
import com.example.rentalreview.screen.myReviews.ReviewEditEntryScreen
import com.example.rentalreview.screen.openScreen.OpenScree
import com.example.rentalreview.screen.perfil.ProfileScreen
import com.example.rentalreview.screen.review.ReviewEntryScreen
import com.example.rentalreview.screen.signUp.SignUpScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalReviewApp(navController: NavHostController = rememberNavController()) {

    NavHost(
        navController = navController,
        startDestination = Screens.OPEN_SCREEN.name
    ){
        composable(route = Screens.OPEN_SCREEN.name){
            OpenScree(
                onGetStarted = {
                    navController.navigate(Screens.LOGIN_SCREEN.name)
                }
            )
        }

        composable(route = Screens.LOGIN_SCREEN.name){
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screens.FEED_SCREEN.name)
                },
                onSignUpClick = {
                    navController.navigate(Screens.SIGN_UP_SCREEN.name)
                }
            )
        }

        composable(route = Screens.SIGN_UP_SCREEN.name){
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screens.LOGIN_SCREEN.name)
                },
                onLoginClick = {
                    navController.navigate(Screens.LOGIN_SCREEN.name)
                }
            )
        }

        composable(route = Screens.FEED_SCREEN.name){
            FeedScreen(
                onSave = {
                    navController.navigate(Screens.FEED_SCREEN.name) {
                        popUpTo(Screens.FEED_SCREEN.name) {
                            inclusive = true
                        }
                    }
                },
                onMyReviewsClick = {
                    navController.navigate(Screens.MY_REVIEWS_SCREEN.name)
                },
                navAfterLogOut = {
                    navController.navigate(Screens.LOGIN_SCREEN.name){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Screens.PROFILE_SCREEN.name){
            ProfileScreen(
                onMyReviewsClick = {
                    navController.navigate(Screens.MY_REVIEWS_SCREEN.name)
                },
                onMyFavoritesClick = {},
                onSettings = {},
                navAfterLogOut = {
                    navController.navigate(Screens.LOGIN_SCREEN.name){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Screens.REVIEW_ENTRY_SCREEN.name){
            ReviewEntryScreen()
        }

        composable(route = "${Screens.EDIT_REVIEW_SCREEN.name}/{reviewId}") {
            ReviewEditEntryScreen()
        }

        composable(route = Screens.FAVORITES_SCREEN.name){
            FavoritesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screens.MY_REVIEWS_SCREEN.name){
            MyReviewsScreen(
                onEditReviewClick = {
                    navController.navigate("${Screens.EDIT_REVIEW_SCREEN.name}/$it")
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}