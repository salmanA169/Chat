package com.swalif.sa.features.main.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swalif.sa.R
import com.swalif.sa.Screens

fun NavGraphBuilder.exploreDestination(navController: NavController){
    composable(Screens.MainScreens.ExploreScreen.route){
        ExploreScreen(navController)
    }
}

@Composable
fun ExploreScreen(
    navController: NavController,
    exploreViewModel :ExploreViewModel = hiltViewModel()
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.align(Alignment.Center)) {
            Button(onClick = { navController.navigate(Screens.SearchScreen.route)}) {
                Text(text = stringResource(id = R.string.search))
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.add_user_by_uid))
            }
        }
    }
}