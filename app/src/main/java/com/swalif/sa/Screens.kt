package com.swalif.sa

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val ON_BOARDING_SCREEN = "onboard_route"
const val MY_UID_ARG = "my_UID"
const val CHANNEL_ID_ARG = "channel_id"
const val PREVIEW_IMAGE_ARG = "preview_image_arg"

sealed class Screens(val route: String) {

    abstract val args: List<NamedNavArgument>

    sealed class OnBoardingScreen(val route: String) {
        object RegistrationScreen : OnBoardingScreen("registration_rout")
        object InformationScreen : OnBoardingScreen("gender_rout")
        object SignInScreen : OnBoardingScreen("login_route")
    }

    sealed class MainScreens(route: String, @StringRes val name: Int, val icon: ImageVector) :
        Screens(route) {
        object HomeScreen : MainScreens("home_route", R.string.home, Icons.Default.Home) {
            override val args: List<NamedNavArgument>
                get() = emptyList()
        }

        object AccountScreen :
            MainScreens("account_route", R.string.account, Icons.Default.AccountCircle) {
            override val args: List<NamedNavArgument>
                get() = emptyList()
        }

        object ExploreScreen :
            MainScreens("explore_route", R.string.explore, Icons.Default.Search) {
            override val args: List<NamedNavArgument>
                get() = emptyList()
        }
    }

    object PreviewScreen : Screens("preview_route") {
        override val args: List<NamedNavArgument>
            get() = listOf(navArgument(PREVIEW_IMAGE_ARG) {
                type = NavType.StringType
            })
        val formattedPreviewRoute = "$route/{$PREVIEW_IMAGE_ARG}"

        fun navigateToPreview(image: String): String {
            val encodedUri = URLEncoder.encode(image, StandardCharsets.UTF_8.toString())
            return buildString {
                append(route)
                val args = listOf(encodedUri)
                args.forEach {
                    append("/$it")
                }
            }
        }
    }

    object SearchScreen : Screens("search_route") {
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    object MessageScreen : Screens("message_rout") {

        val formattedMessageRoute = "$route/{$CHANNEL_ID_ARG}/{$MY_UID_ARG}"
        override val args: List<NamedNavArgument>
            get() = listOf(
                navArgument(CHANNEL_ID_ARG) {
                    type = NavType.IntType
                },
                navArgument(MY_UID_ARG) {
                    type = NavType.StringType
                }
            )

        fun navigateToMessageScreen(myUid: String, channelId: String): String {
            return buildString {
                append(route)
                val args = listOf(channelId, myUid)
                args.forEach {
                    append("/$it")
                }
            }
        }
    }
}

val listMainScreens = listOf(
    Screens.MainScreens.HomeScreen,
    Screens.MainScreens.ExploreScreen,
    Screens.MainScreens.AccountScreen
)
