package com.swalif.sa.features.onboarding.registration.registration

import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.features.main.home.message.EditTextField
import com.swalif.sa.ui.theme.ChatAppTheme
import logcat.logcat

fun NavGraphBuilder.registrationDest(navController: NavController) {
    composable(
        Screens.OnBoardingScreen.RegistrationScreen.route,
        arguments = Screens.OnBoardingScreen.RegistrationScreen.args
    ) {
        val registrationViewModel: RegistrationViewModel = hiltViewModel()
        val registrationState by registrationViewModel.currentRegistrationState.collectAsStateWithLifecycle()
        RegistrationScreen(
            registrationState,
            onSignIn = registrationViewModel::signInGoogleOneTap,
            onNavigate = {
                navController.navigate(it) {
                    popUpTo(Screens.OnBoardingScreen.RegistrationScreen.route) {
                        inclusive = true
                    }
                }
            }, onSignInResult = registrationViewModel::signInResult
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true, wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun Preview() {
    ChatAppTheme() {
        RegistrationScreen(RegistrationState())
    }
}

@Composable
fun RegistrationScreen(
    registrationState: RegistrationState,
    onSignIn: () -> Unit = {},
    onSignInResult: (Intent) -> Unit = {},
    onNavigate: (route: String) -> Unit = {}
) {

    val registerIntentSender =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            onSignInResult(it.data ?: return@rememberLauncherForActivityResult)
        }

    val context = LocalContext.current
    LaunchedEffect(key1 = registrationState.userData) {
        if (registrationState.userData != null) {
            val user = registrationState.userData
            if (user.isNewUser) {
                onNavigate(
                    Screens.OnBoardingScreen.InformationScreen.navigateToInformation(
                        user.email!!,
                        user.photo!!,
                        user.userId!!,
                        user.username!!
                    )
                )
            } else {
                onNavigate(
                    Screens.MainScreens.HomeScreen.route
                )
                Toast.makeText(context, "Welcome back ${user.username}", Toast.LENGTH_SHORT).show()
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
    // TODO: continue here implement email and password for firebase
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painterResource(id = R.drawable.logo_chat1),
            contentDescription = "",
            modifier = Modifier.aspectRatio(3f)
        )
        TextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth())
        TextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.signIn))

            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "or Sign up")
        }

        Divider(Modifier.fillMaxWidth(0.9f))
        Button(modifier = Modifier, onClick = {
            onSignIn()
        }) {
            Image(painterResource(id = R.drawable.google_icon), contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Continue with google")
        }
    }
}