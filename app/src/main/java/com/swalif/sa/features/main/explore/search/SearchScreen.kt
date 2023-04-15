package com.swalif.sa.features.main.explore.search

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.ui.theme.ChatAppTheme

fun NavGraphBuilder.searchScreen(navController: NavController) {
    composable(Screens.SearchScreen.route) {
        SearchScreen(navController)
    }
}

@Preview
@Composable
fun Preview2() {
    ChatAppTheme() {
        SearchScreen(navController = rememberNavController())
    }
}

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchState by searchViewModel.searchState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        ElevatedCard(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
//            AsyncImage(model = Icons.Default.Email, contentDescription = "",modifier = Modifier.drawBehind {
//                drawArc(
//                    Color.Green,0f,260f,false,style = Stroke(2f)
//                )
//            })
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                    })
        }
    }
}

@Composable
fun ImageProgressIndicator() {

}