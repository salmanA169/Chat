package com.swalif.sa.features.onboarding.signup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.ui.theme.ChatAppTheme

fun NavGraphBuilder.signupDest(navController: NavController) {
    composable(Screens.OnBoardingScreen.SignUpScreen.route) {
        val signupViewModel: SignupViewModel = hiltViewModel()
        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
                signupViewModel.updateImage(
                    it?.toString() ?: return@rememberLauncherForActivityResult
                )
            }
        val state by signupViewModel.state.collectAsStateWithLifecycle()
        val event by signupViewModel.event.collectAsStateWithLifecycle()
        val context = LocalContext.current
        LaunchedEffect(key1 = event ){
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

                is SignUpEvent.MessageError -> {
                    Toast.makeText(
                        context,
                        (event as SignUpEvent.MessageError).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        SignUpScreen(
            signupState = state,
            onSelectImage = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            onSignup = signupViewModel::signUp,
            onUsernameChange = signupViewModel::updateUserName,
            onPasswordChange = signupViewModel::updatePassword,
            onEmailChange = signupViewModel::updateEmail
        )
    }
}

@Preview
@Composable
fun SignupPreview() {
    ChatAppTheme {
        SignUpScreen()
    }
}

@Composable
fun SignUpScreen(
    signupState: SignupState = SignupState(),
    onSelectImage: () -> Unit = {},
    onSignup: () -> Unit = {},
    onUsernameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
        TextField(
            value = signupState.username,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.username_label)) },
            isError = signupState.showUserNameError,
            supportingText = {
                if (signupState.showUserNameError) {
                    Text(text = "user name must not be empty")
                }
            }
        )
        TextField(
            value = signupState.email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.email_label)) },
            isError = signupState.showEmailError,
            supportingText = {
                if (signupState.showEmailError) {
                    Text(text = "Invalid email")
                }
            })
        TextField(
            value = signupState.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.password)) },
            isError = signupState.showPasswordError,
            supportingText = {
                if (signupState.showPasswordError) {
                    Text(text = "Invalid password")
                }
            }, visualTransformation = PasswordVisualTransformation())
        Button(onClick = onSignup, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.signup))
        }
    }
}