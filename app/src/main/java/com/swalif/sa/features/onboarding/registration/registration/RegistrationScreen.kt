package com.swalif.sa.features.onboarding.registration.registration

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swalif.sa.Screens
import com.swalif.sa.ui.theme.ChatAppTheme
import logcat.logcat

fun NavGraphBuilder.registrationDest(navController: NavController) {
    composable(Screens.OnBoardingScreen.RegistrationScreen.route, arguments = Screens.OnBoardingScreen.RegistrationScreen.args) {
        val registrationViewModel: RegistrationViewModel = hiltViewModel()
        val registrationState by registrationViewModel.currentRegistrationState.collectAsStateWithLifecycle()
        RegistrationScreen(
            registrationState,
            onSignIn = registrationViewModel::signInGoogleOneTap,
            onNavigate = {
            navController.navigate(it){
                popUpTo(Screens.OnBoardingScreen.RegistrationScreen.route){
                    inclusive = true
                }
            }
            }, onSignInResult = registrationViewModel::signInResult)
    }
}

@Preview
@Composable
fun Preview() {
    ChatAppTheme() {
//        RegistrationScreen()
    }
}

@Composable
fun RegistrationScreen(
    registrationState: RegistrationState,
    onSignIn: () -> Unit,
    onSignInResult: (Intent)->Unit,
    onNavigate: (route: String) -> Unit
) {

    val registerIntentSender =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            onSignInResult(it.data!!)
        }

    val context = LocalContext.current
    LaunchedEffect(key1 = registrationState.userData) {
        if (registrationState.userData != null) {
            val user = registrationState.userData
            if (user.isNewUser){
                onNavigate(
                    Screens.OnBoardingScreen.InformationScreen.navigateToInformation(
                        user.email!!,
                        user.photo!!,
                        user.userId!!,
                        user.username!!
                    )
                )
            }else{
                // TODO: navigateBack to home screen and fetch data from firestorte
                Toast.makeText(context, "Home screen", Toast.LENGTH_SHORT).show()
            }
        }
    }
    LaunchedEffect(key1 = registrationState.showGoogleSignInt) {
        if (registrationState.showGoogleSignInt != null) {
            registerIntentSender.launch(
                IntentSenderRequest.Builder(registrationState.showGoogleSignInt).build()
            )
        } else {
            logcat("RegistrationScreen") {
                "LaunchedEffect: intent is null"
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Button(modifier = Modifier.align(Alignment.Center), onClick = {
            onSignIn()
        }) {
            Text(text = "Continue with google")
        }
    }
}