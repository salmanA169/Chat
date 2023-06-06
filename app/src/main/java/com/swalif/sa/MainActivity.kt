package com.swalif.sa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.swalif.sa.component.*
import com.swalif.sa.features.main.account.accountScreen
import com.swalif.sa.features.main.explore.exploreDestination
import com.swalif.sa.features.main.home.homeDest
import com.swalif.sa.features.main.home.message.messageDest
import com.swalif.sa.features.main.home.message.previewImage.previewNavDest
import com.swalif.sa.features.main.explore.search.searchScreen
import com.swalif.sa.features.onboarding.onBoardingNavigation
import com.swalif.sa.ui.theme.ChatAppTheme
import dagger.hilt.android.AndroidEntryPoint
import logcat.logcat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var launcherPermission: ActivityResultLauncher<Array<String>>

    private val viewModel by viewModels<MainViewModel>()
    override fun onPause() {
        super.onPause()
        viewModel.setOffline()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setOnline()
    }
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false)

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
                var titleBar by remember{
                    mutableStateOf(R.string.home)
                }
                val rememberClickNav = remember{
                    {screen:Screens.MainScreens->
                        navController.navigate(screen.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        titleBar = screen.name
                    }
                }
                LaunchedEffect(key1 = isUserAvailable) {
                    isUserAvailable?.let { isAvailable ->
                        if (!isAvailable) {
                            navController.navigate(Screens.OnBoardingScreen.RegistrationScreen.route) {
                                popUpTo(Screens.MainScreens.HomeScreen.route) {
                                    inclusive = true
                                }
                            }
                            // for fetch data again in viewModel
                            navController.clearBackStack(Screens.MainScreens.ExploreScreen.route)
                            viewModel.nukeChatAndMessageTables()
                        }
                    }
                }
                Scaffold(
                    topBar = {
                        AnimatedVisibility(visible = isMainDest, modifier = Modifier.wrapContentSize()) {
                            MediumTopAppBar(scrollBehavior = scrollTopBar, title = {
                                Text(text = stringResource(id = titleBar))
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
                                            rememberClickNav(screen)
                                        },
                                        icon = {
                                            Icon(imageVector = screen.icon, contentDescription = "")
                                        }
                                    )
                                }
                            }
                        }

                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Screens.MainScreens.HomeScreen.route
                    ) {
                        homeDest(navController, paddingValues, scrollTopBar.nestedScrollConnection)
                        onBoardingNavigation(navController)
                        messageDest(navController)
                        previewNavDest(navController)
                        searchScreen(navController)
                        exploreDestination(navController,paddingValues)
                        accountScreen(navController,paddingValues)
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