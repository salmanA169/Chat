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
import logcat.logcat

const val ON_BOARDING_SCREEN = "onboard_route"
 const val MY_UID_ARG = "my_UID"
 const val CHANNEL_ID_ARG = "channel_id"

sealed class Screens(val route: String) {

    abstract val args :List<NamedNavArgument>

    sealed class OnBoardingScreen(val route: String) {
        object RegistrationScreen : OnBoardingScreen("registration_rout")
        object InformationScreen : OnBoardingScreen("gender_rout")
        object SignInScreen : OnBoardingScreen("login_route")
    }

    sealed class MainScreens(route: String, @StringRes val name: Int, val icon: ImageVector) :
        Screens(route) {
        object HomeScreen : MainScreens("home_route", R.string.home, Icons.Default.Home){
            override val args: List<NamedNavArgument>
                get() = emptyList()
        }
        object AccountScreen :
            MainScreens("account_route", R.string.account, Icons.Default.AccountCircle){
            override val args: List<NamedNavArgument>
                get() = emptyList()
        }

        object SearchScreen : MainScreens("search_route", R.string.search, Icons.Default.Search){
            override val args: List<NamedNavArgument>
                get() = emptyList()
        }
    }


    init {
        logcat { route }
    }
    object MessageScreen : Screens("message_rout"){

        val formattedMessageRoute = "$route/{$CHANNEL_ID_ARG}/{$MY_UID_ARG}"
        override val args: List<NamedNavArgument>
            get() = listOf(
                navArgument(CHANNEL_ID_ARG){
                    type = NavType.IntType
                },
                navArgument(MY_UID_ARG){
                    type = NavType.StringType

                }
            )
        fun navigateToMessageScreen(myUid:String,channelId:String):String{
            return buildString {
                append(route)
                val args = listOf(channelId,myUid)
                args.forEach {
                    append("/$it")
                }
            }
        }
    }
}

val listMainScreens = listOf(
    Screens.MainScreens.HomeScreen,
    Screens.MainScreens.SearchScreen,
    Screens.MainScreens.AccountScreen
)
