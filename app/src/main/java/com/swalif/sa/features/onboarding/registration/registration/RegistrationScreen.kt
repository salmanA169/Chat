package com.swalif.sa.features.onboarding.registration.registration

import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.ui.theme.ChatAppTheme
import logcat.logcat

fun NavGraphBuilder.registrationDest(navController: NavController) {
    composable(
        Screens.OnBoardingScreen.RegistrationScreen.route,
        arguments = Screens.OnBoardingScreen.RegistrationScreen.args
    ) {
        val registrationViewModel: RegistrationViewModel = hiltViewModel()
        val registrationState by registrationViewModel.currentRegistrationState.collectAsStateWithLifecycle()
        val event by registrationViewModel.event.collectAsStateWithLifecycle()
        val context = LocalContext.current
        LaunchedEffect(key1 = event) {
            when (event) {
                is RegistrationEvent.ErrorMessage -> {
                    Toast.makeText(context, (event as RegistrationEvent.ErrorMessage).message, Toast.LENGTH_SHORT).show()
                }
                is RegistrationEvent.NavigateHomeScreen -> {
                    navController.navigate(Screens.MainScreens.HomeScreen.route) {
                        popUpTo(Screens.OnBoardingScreen.RegistrationScreen.route) {
                            inclusive = true
                        }
                    }
                    Toast.makeText(context, "Welcome back ${(event as RegistrationEvent.NavigateHomeScreen).userName}", Toast.LENGTH_SHORT).show()
                }

                is RegistrationEvent.NavigateInfo -> {
                    val castEvent = event as RegistrationEvent.NavigateInfo
                    navController.navigate(
                        Screens.OnBoardingScreen.InformationScreen.navigateToInformation(
                            castEvent.email,
                            castEvent.photo,
                            castEvent.uidUser,
                            castEvent.username
                        )
                    )
                }

                null ->{}
            }
        }
        RegistrationScreen(
            registrationState,
            onGoogleSignIn = registrationViewModel::signInGoogleOneTap,
            onSignInResult = registrationViewModel::signInResult,
            onSignUp = {
                navController.navigate(it)
            },
            onEmailChange = registrationViewModel::updateEmail,
            onPasswordChange = registrationViewModel::updatePassword,
            onSignInClick = registrationViewModel::signIn
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
    onGoogleSignIn: () -> Unit = {},
    onSignInResult: (Intent) -> Unit = {},
    onSignUp: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onSignInClick: () -> Unit = {}
) {

    val registerIntentSender =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            onSignInResult(it.data ?: return@rememberLauncherForActivityResult)
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painterResource(id = R.drawable.logo_chat1),
            contentDescription = "",
            modifier = Modifier.aspectRatio(3f)
        )
        TextField(
            value = registrationState.email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = stringResource(id = R.string.email_label))
            }, isError = registrationState.showEmailError, supportingText = {
                if (registrationState.showEmailError) {
                    Text(text = "Invalid Email")
                }
            })
        TextField(
            value = registrationState.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = stringResource(id = R.string.password))
            }, isError = registrationState.showPasswordError, supportingText = {
                if (registrationState.showPasswordError) {
                    Text(text = "Invalid Password")
                }
            }, visualTransformation = PasswordVisualTransformation())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { onSignInClick() }) {
                Text(text = stringResource(id = R.string.signIn))

            }
            Spacer(modifier = Modifier.size(16.dp))
            ClickableText(
                style = LocalTextStyle.current.copy(textDecoration = TextDecoration.Underline),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Or Sign up")
                    }
                },
                onClick = {
                    onSignUp(
                        Screens.OnBoardingScreen.SignUpScreen.route
                    )
                })

        }

        Divider(Modifier.fillMaxWidth(0.9f))
        Button(modifier = Modifier, onClick = {
            onGoogleSignIn()
        }) {
            Image(painterResource(id = R.drawable.google_icon), contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Continue with google")
        }
    }
}