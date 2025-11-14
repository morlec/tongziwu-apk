package com.tongziwu.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.tongziwu.app.ui.screens.HomeScreen
import com.tongziwu.app.ui.screens.HandwritingScreen
import com.tongziwu.app.ui.screens.QueryScreen

object Routes {
    const val Home = "home"
    const val Handwriting = "handwriting"
    const val Query = "query/{char}"
}

@Composable
fun AppNav(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Routes.Home) {
        composable(Routes.Home) { HomeScreen(
            onHandwriting = { navController.navigate(Routes.Handwriting) },
            onSearchChar = { navController.navigate("query/$it") }
        ) }
        composable(Routes.Handwriting) { HandwritingScreen(
            onPick = { navController.navigate("query/$it") },
            onBack = { navController.popBackStack() }
        ) }
        composable(Routes.Query) { backStackEntry ->
            val char = backStackEntry.arguments?.getString("char")
            QueryScreen(char ?: "")
        }
    }
}