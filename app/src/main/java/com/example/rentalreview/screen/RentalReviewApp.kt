package com.example.rentalreview.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rentalreview.Screens
import com.example.rentalreview.screen.login.LoginScreen
import com.example.rentalreview.screen.openScreen.OpenScree
import com.example.rentalreview.screen.signUp.SignUpScreen


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
                    navController.navigate(Screens.HOME_SCREEN.name)
                },
                onSignUpClick = {
                    navController.navigate(Screens.SIGN_UP_SCREEN.name)
                }
            )
        }

        composable(route = Screens.SIGN_UP_SCREEN.name){
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screens.HOME_SCREEN.name)
                }
            )
        }
    }
}