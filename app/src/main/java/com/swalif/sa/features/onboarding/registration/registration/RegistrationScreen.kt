package com.swalif.sa.features.onboarding.registration.registration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swalif.sa.Screens
import com.swalif.sa.ui.theme.ChatAppTheme

fun NavGraphBuilder.registrationDest(navController: NavController) {
    composable(Screens.OnBoardingScreen.RegistrationScreen.route){
        RegistrationScreen(navController)
    }
}

@Preview
@Composable
fun Preview() {
    ChatAppTheme() {
        RegistrationScreen(navController = rememberNavController())
    }
}

@Composable
fun RegistrationScreen(
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()){
        Button(modifier = Modifier.align(Alignment.Center),onClick = {
            navController.navigate(Screens.OnBoardingScreen.InformationScreen.route)
        }) {
            Text(text = "Continue with google")
        }
    }
}