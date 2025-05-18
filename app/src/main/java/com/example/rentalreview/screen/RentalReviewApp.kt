package com.example.rentalreview.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rentalreview.Screens
import com.example.rentalreview.screen.openScreen.OpenScree


@Composable
fun RentalReviewApp(navController: NavHostController = rememberNavController()) {

    NavHost(
        navController = navController,
        startDestination = Screens.OPEN_SCREEN.name
    ){
        composable(route = Screens.HOME_SCREEN.name){
            OpenScree(
                onGetStarted = {
                    navController.navigate(Screens.LOGIN_SCREEN.name)
                }
            )
        }
    }
}