package com.example.rentalreview.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rentalreview.Screens
import com.example.rentalreview.screen.home.FeedScreen
import com.example.rentalreview.screen.login.LoginScreen
import com.example.rentalreview.screen.openScreen.OpenScree
import com.example.rentalreview.screen.perfil.PerfilScreen
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
            FeedScreen(onSave = {
                navController.navigate(Screens.FEED_SCREEN.name){
                    popUpTo(Screens.FEED_SCREEN.name){
                        inclusive = true
                    }
                }
            })
        }

        composable(route = Screens.PROFILE_SCREEN.name){
            PerfilScreen()
        }

        composable(route = Screens.REVIEW_ENTRY_SCREEN.name){
            ReviewEntryScreen()
        }
    }
}