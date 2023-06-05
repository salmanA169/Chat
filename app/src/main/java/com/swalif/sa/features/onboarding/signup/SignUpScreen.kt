package com.swalif.sa.features.onboarding.signup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.swalif.sa.R
import com.swalif.sa.Screens

fun NavGraphBuilder.signupDest(navController: NavController) {
    composable(Screens.OnBoardingScreen.SignUpScreen.route) {
        val signupViewModel: SignupViewModel = hiltViewModel()
        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
                signupViewModel.updateImage(it?.toString()?:return@rememberLauncherForActivityResult)
            }
        val state by signupViewModel.state.collectAsStateWithLifecycle()
        val event by signupViewModel.event.collectAsStateWithLifecycle()
        when (event) {
            is SignUpEvent.Navigate -> {
                val result = event as SignUpEvent.Navigate
                navController.navigate(
                    Screens.OnBoardingScreen.InformationScreen.navigateToInformation(
                        result.email,
                        result.image,
                        result.uid,
                        result.name
                    )
                )
            }

            null -> {

            }

        }
        SignUpScreen(
            signupState = state,
            onSelectImage = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            onSignup = signupViewModel::signUp
        )
    }
}

@Composable
fun SignUpScreen(
    signupState: SignupState,
    onSelectImage: () -> Unit,
    onSignup: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        if (signupState.showProgress) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    if (signupState.image.isEmpty()) {
                        onSelectImage()
                    }
                },
            contentDescription = "",
            model = signupState.image,
            error = painterResource(id = R.drawable.image_icon),
            contentScale = ContentScale.Crop
        )
    }
}