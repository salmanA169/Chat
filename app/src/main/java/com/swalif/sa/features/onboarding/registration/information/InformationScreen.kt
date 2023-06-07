package com.swalif.sa.features.onboarding.registration.information

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.swalif.Constants
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.component.Gender
import com.swalif.sa.component.GenderPicker
import com.swalif.sa.component.GenderState
import com.swalif.sa.component.rememberGenderState
import com.swalif.sa.ui.theme.ChatAppTheme
import logcat.logcat

fun NavGraphBuilder.informationDest(navController: NavController) {
    composable(Screens.OnBoardingScreen.InformationScreen.formattedInfoRout) {
        val informationViewModel: InformationViewModel = hiltViewModel()
        val infoState by informationViewModel.infoState.collectAsState()
        val infoEvent by informationViewModel.infoEvent.collectAsState()
        val genderState = rememberGenderState()

        LaunchedEffect(key1 = infoEvent) {
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
        RegistrationInformation(
            infoState,
            genderState,
            informationViewModel::updateUsername,
            informationViewModel::updateEmail,
            informationViewModel::checkAndSave
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationPreview() {
    ChatAppTheme {
        RegistrationInformation()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationInformation(
    infoState: InformationState = InformationState(),
    genderState: GenderState = GenderState(),
    onUsernameChange: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onSaveClick: (Gender) -> Unit = {}
) {

    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(), verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (infoState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .align(CenterHorizontally),
            contentDescription = "",
            model = infoState.imageUri,
            contentScale = ContentScale.Crop
        )

        OutlinedTextField(
            leadingIcon = {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "")
            },
            isError = infoState.showNameError,
            supportingText = {
                if (infoState.showNameError) {
                    Text("user name must not be empty")
                }
            }, label = {
                Text(text = stringResource(id = R.string.username))
            },
            modifier = Modifier.fillMaxWidth(),
            value = infoState.name,
            onValueChange = onUsernameChange
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = infoState.email,
            readOnly = true,
            onValueChange = onEmailChanged,
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
        val annotatedString = buildAnnotatedString {
            append("By creating account you accept ")
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary
                )
            ) {
                pushStringAnnotation(tag = privacy, annotation = privacy)
                append(privacy)
            }
        }

        ClickableText(modifier = Modifier.padding(6.dp),
            style = MaterialTheme.typography.labelMedium.copy(LocalContentColor.current),
            text = annotatedString,
            onClick = {
                annotatedString.getStringAnnotations(it, it).firstOrNull()?.let {
                    uriHandler.openUri(Constants.URI_PRIVACY)
                }
            })
        Button(modifier = Modifier.align(CenterHorizontally), onClick = {
            onSaveClick(genderState.currentGender.value)
        }) {
            Text(text = "confirm")
        }
    }
}

private const val privacy = "Privacy policy"