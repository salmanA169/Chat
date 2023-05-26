package com.swalif.sa.features.main.home.message.previewImage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.swalif.sa.Screens

fun NavGraphBuilder.previewNavDest(navController: NavController){
    composable(Screens.PreviewScreen.formattedPreviewRoute,Screens.PreviewScreen.args){
        PreviewScreen(navController = navController)
    }
}

@Composable
fun PreviewScreen(
    viewModel: PreviewViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.previewState.collectAsState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        AsyncImage(model = state, contentDescription = "",modifier = Modifier.fillMaxSize())
    }
}