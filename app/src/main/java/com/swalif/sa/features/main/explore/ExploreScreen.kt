package com.swalif.sa.features.main.explore

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.component.Gender
import com.swalif.sa.component.drawAnimatedColoredShadow
import com.swalif.sa.model.UserInfo
import com.swalif.sa.ui.theme.ChatAppTheme

fun NavGraphBuilder.exploreDestination(navController: NavController, paddingValues: PaddingValues) {
    composable(Screens.MainScreens.ExploreScreen.route) {
        val exploreViewModel = hiltViewModel<ExploreViewModel>()
        val exploreState by exploreViewModel.state.collectAsStateWithLifecycle()
        val exploreEvent by exploreViewModel.event.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = exploreEvent) {
            exploreEvent?.let {
                when (it) {
                    is ExploreEvent.Navigate -> {
                        navController.navigate(it.route)
                    }

                    is ExploreEvent.NavigateToChat -> {
                        navController.navigate(
                            Screens.MessageScreen.navigateToMessageScreen(
                                it.myUid,
                                it.chatId,
                                it.isSavedLocally
                            )
                        )
                    }
                }
                exploreViewModel.readEvent()
            }
        }
        ExploreScreen(exploreState, paddingValues, exploreViewModel::onEvent)
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE, showBackground = true
)
@Composable
fun ExploreScreenPreview() {
    ChatAppTheme {
        ExploreScreen(
            ExploreState(
                listOf(
                    UserInfo("", "salman alamoudi", "", Gender.MALE, "sewd", 0, "."),
                    UserInfo("", "salman alamoudi", "", Gender.MALE, "sq", 0, "."),
                    UserInfo("", "salman alamoudi", "", Gender.MALE, "a", 0, "."),
                    UserInfo("", "salman alamoudi", "", Gender.FEMALE, "s", 0, "."),
                    UserInfo("", "salman alamoudi", "", Gender.MALE, "cxzc", 0, "."),
                    UserInfo("", "salman alamoudi", "", Gender.MALE, "dsad", 0, "."),
                    UserInfo("", "salman alamoudi", "", Gender.FEMALE, "ewqeq", 0, "."),
                )
            ), PaddingValues()
        )
    }
}

@Composable
fun ExploreScreen(
    exploreState: ExploreState,
    paddingValues: PaddingValues,
    onEvent: (UiExploreEvent) -> Unit = {}
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        SearchIcon(onClick = { onEvent(UiExploreEvent.NavigateToSearch)}, modifier = Modifier.align(Center))
    }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(paddingValues)
//    ) {
//        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//            Button(onClick = { onEvent(UiExploreEvent.NavigateToSearch)}) {
//                Icon(Icons.Default.Search, contentDescription = "")
//                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
//                Text(text = stringResource(id = R.string.find_random_user))
//            }
//            Button(onClick = { /*TODO*/ }) {
//                Text(text = stringResource(id = R.string.add_user_by_uid))
//            }
//        }
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(2.dp),
//            horizontalArrangement = Arrangement.spacedBy(2.dp)
//        ) {
//            items(exploreState.users, key = {
//                it.uniqueId
//            }) {
//                UsersCard(userInfo = it){
//                    onEvent(UiExploreEvent.NavigateToChat(it))
//                }
//            }
//        }
//    }
}

@Composable
fun SearchIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .drawAnimatedColoredShadow(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.inversePrimary,
                alpha = 0.6f,
                shadowRadius = 25.dp,
                borderRadius = 10.dp, onClick = onClick
            )
            .size(200.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier
                .fillMaxSize(0.7f)
                .align(Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersCard(
    userInfo: UserInfo,
    onClick: (UserInfo) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), onClick = {
            onClick(userInfo)
        }
    ) {

        AsyncImage(
            model = userInfo.photoUri,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
        )
        Row(modifier = Modifier.align(CenterHorizontally)) {
            Text(text = userInfo.userName)
            Icon(
                painter = painterResource(id = userInfo.gender.getGenderIcon()),
                contentDescription = "gender icon",
                tint = userInfo.gender.getSolidColorByGender()
            )
        }

    }
}