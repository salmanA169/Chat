package com.swalif.sa

import android.content.ContentUris
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.swalif.sa.component.*
import com.swalif.sa.features.main.home.homeDest
import com.swalif.sa.features.main.home.message.messageDest
import com.swalif.sa.features.onboarding.onBoardingNavigation
import com.swalif.sa.ui.theme.ChatAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import logcat.logcat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var launcherPermission: ActivityResultLauncher<Array<String>>

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            ChatAppTheme {
                val viewModel = hiltViewModel<MainViewModel>()
                val isUserAvailable by viewModel.isUserAvailable.collectAsState()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDest = navBackStackEntry?.destination
                val isMainDest = listMainScreens.any {
                    it.route == (currentDest?.route ?: "")
                }
                val scrollTopBar = TopAppBarDefaults.enterAlwaysScrollBehavior()
                LaunchedEffect(key1 = isUserAvailable) {
                    isUserAvailable?.let { isAvailable ->
                        if (!isAvailable) {
                            navController.navigate(Screens.OnBoardingScreen.RegistrationScreen.route) {
                                popUpTo(Screens.MainScreens.HomeScreen.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
                Scaffold(
                    topBar = {
                        AnimatedVisibility(visible = isMainDest) {
                            MediumTopAppBar(scrollBehavior = scrollTopBar,title = {
                                Text(text = "Main screen")
                            })
                        }
                    },
                    bottomBar = {
                        AnimatedVisibility(visible = isMainDest) {
                            NavigationBar() {
                                listMainScreens.forEach { screen ->
                                    NavigationBarItem(
                                        label = {
                                            Text(text = stringResource(id = screen.name))
                                        },
                                        selected = currentDest?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route){
                                                popUpTo(navController.graph.findStartDestination().id){
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = {
                                            Icon(imageVector = screen.icon, contentDescription = "")
                                        }
                                    )
                                }
                            }
                        }

                    }
                ) {paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Screens.MainScreens.HomeScreen.route
                    ) {
                        
                        homeDest(navController,paddingValues,scrollTopBar.nestedScrollConnection)
                        onBoardingNavigation(navController)
                        messageDest(navController)
                        composable(Screens.MainScreens.SearchScreen.route){

                        }
                        composable(Screens.MainScreens.AccountScreen.route){
                            Button(modifier = Modifier.padding(paddingValues),onClick = {
                                viewModel.signOut()
                            }) {
                                Text(text = "sign out")
                            }
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChatAppTheme {

    }
}