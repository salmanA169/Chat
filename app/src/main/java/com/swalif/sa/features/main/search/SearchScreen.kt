package com.swalif.sa.features.main.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swalif.sa.Screens

fun NavGraphBuilder.searchScreen(navController: NavController){
    composable(Screens.MainScreens.SearchScreen.route){
        SearchScreen()
    }
}

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchState = searchViewModel.searchState.collectAsStateWithLifecycle()

}