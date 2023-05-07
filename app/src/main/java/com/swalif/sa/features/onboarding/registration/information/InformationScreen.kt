package com.swalif.sa.features.onboarding.registration.information

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.swalif.sa.Screens
import com.swalif.sa.component.GenderPicker
import com.swalif.sa.component.rememberGenderState
import logcat.logcat

fun NavGraphBuilder.informationDest(navController: NavController) {
    composable(Screens.OnBoardingScreen.InformationScreen.formattedInfoRout) {
        RegistrationInformation(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationInformation(
    navController: NavController,
    viewModel: InformationViewModel = hiltViewModel()
) {
    val infoState by viewModel.infoState.collectAsState()
    val infoEvent by viewModel.infoEvent.collectAsState()
    val genderState = rememberGenderState()

    LaunchedEffect(key1 = infoEvent ){
        when (infoEvent) {
            InfoEvent.Saved -> {
                navController.navigate(Screens.MainScreens.HomeScreen.route) {
                    popUpTo(Screens.OnBoardingScreen.InformationScreen.route) {
                        inclusive = true
                    }
                }
            }
            null -> {
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (!infoState.isLoading){
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .align(CenterHorizontally),
            contentDescription = "",
            model = infoState.imageUri
        )

        OutlinedTextField(
            leadingIcon = {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "")
            },
            isError = infoState.showNameError,
            supportingText = {
                if (infoState.showNameError){
                    Text("user name must not be empty")
                }
            },label = {
                Text(text = "Username")
            },
            modifier = Modifier.fillMaxWidth(),
            value = infoState.name,
            onValueChange = {
                viewModel.updateUsername(it)
            })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = infoState.email,
            onValueChange = {
                viewModel.updateEmail(it)
            },
            isError = infoState.showEmailError,
            label = {
                Text(text = "Email")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "")
            },
            supportingText = {
                if (infoState.showEmailError) {
                    Text(text = "Invalid Email")
                }
            }
        )

        GenderPicker(
            modifier = Modifier.fillMaxWidth(),
            genderState = genderState,
            shouldShowError = infoState.showGenderError
        )
        Button(modifier = Modifier.align(CenterHorizontally), onClick = {
            viewModel.checkAndSave(genderState.currentGender.value)
        }) {
            Text(text = "confirm")
        }
    }
}