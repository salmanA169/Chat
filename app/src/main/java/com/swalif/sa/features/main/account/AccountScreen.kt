package com.swalif.sa.features.main.account

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.component.CustomRipple
import com.swalif.sa.component.Gender
import com.swalif.sa.model.UserInfo
import com.swalif.sa.ui.theme.ChatAppTheme
import com.swalif.sa.utils.formatDate
import com.swalif.sa.utils.formatSortTime

fun NavGraphBuilder.accountScreen(navController: NavController, paddingValues: PaddingValues) {
    composable(Screens.MainScreens.AccountScreen.route) {
        val accountViewModel = hiltViewModel<AccountViewModel>()
        val state by accountViewModel.state.collectAsStateWithLifecycle()
        AccountScreen(accountState = state, onSignOut = accountViewModel::signOut, paddingValues,accountViewModel::copyText)
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun AccountPreview() {
    ChatAppTheme {
        AccountScreen(accountState = AccountState(
            UserInfo(
                "",
                "salman alamoudi",
                "salman.alamoudi95@gmail.com",
                Gender.MALE,
                "ss8964aw0",
                Timestamp.now().toDate().time,
                ""
            )
        ), onSignOut = {}, onCopyText = {})
    }
}

@Composable
fun AccountScreen(
    accountState: AccountState,
    onSignOut: (String) -> Unit,
    paddingValues: PaddingValues = PaddingValues(),
    onCopyText:(String)->Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (accountState.userInfo == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .wrapContentHeight(), verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AsyncImage(
                    model = accountState.userInfo.photoUri,
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                        .align(CenterHorizontally)
                        .clip(CircleShape),
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
                )

                ReadEditText(
                    text = accountState.userInfo.userName,
                    prefix = {
                        Text(text = stringResource(id = R.string.username))
                    }
                )
                ReadEditText(
                    text = accountState.userInfo.email,
                    prefix = {
                        Text(text = stringResource(id = R.string.email))
                    }
                )
                ReadEditText(
                    text = accountState.userInfo.createdAt.formatDate(),
                    prefix = {
                        Text(text = stringResource(id = R.string.createAt))
                    }
                )
                ReadEditText(
                    text = accountState.userInfo.uniqueId,
                    prefix = {
                        Text(text = stringResource(id = R.string.uniqueId))
                    },
                    trailingIcon = {
                        IconButton(onClick = { onCopyText(accountState.userInfo.uniqueId) }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.copy_icon),
                                contentDescription = "copy",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
                ReadEditText(
                    text = accountState.userInfo.gender.gender,
                    prefix = {
                        Text(text = stringResource(id = R.string.gender))
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = accountState.userInfo.gender.getSolidColorByGender()
                    )
                )
                CompositionLocalProvider(
                   LocalRippleTheme provides CustomRipple(
                        MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    OutlinedButton(
                        onClick = { onSignOut(accountState.userInfo.uidUser) },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                        ) {
                        Text(text = stringResource(id = R.string.signOut))
                    }
                }

            }
        }
    }
}

@Composable
fun ReadEditText(
    text: String,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = MaterialTheme.colorScheme.primary
    ),
    prefix: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = text,
        onValueChange = {},
        readOnly = true,
        modifier = modifier.fillMaxWidth(),

        prefix = prefix,
        colors = colors,
        trailingIcon = trailingIcon
    )
}

