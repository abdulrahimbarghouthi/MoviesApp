package com.abarghouthi.moviesapp.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.abarghouthi.moviesapp.ui.details.DetailsScreen
import com.abarghouthi.moviesapp.ui.home.HomeScreen
import com.abarghouthi.moviesapp.ui.home.HomeViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {

            HomeScreen(onMovieSelected = { id ->
                navController.navigate("movie/$id")
            })
        }

        composable("movie/{id}", arguments = listOf(navArgument("id"){ type = NavType.IntType }) ) {
            DetailsScreen(id = it.arguments!!.getInt("id"), onBackClicked = { navController.navigateUp() })
        }
    }
}