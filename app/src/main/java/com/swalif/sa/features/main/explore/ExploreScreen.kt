package com.swalif.sa.features.main.explore

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import coil.compose.AsyncImage
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.component.Gender
import com.swalif.sa.model.UserInfo
import com.swalif.sa.ui.theme.ChatAppTheme

fun NavGraphBuilder.exploreDestination(navController: NavController, paddingValues: PaddingValues) {
    composable(Screens.MainScreens.ExploreScreen.route) {
        val exploreViewModel = hiltViewModel<ExploreViewModel>()
        val exploreState by exploreViewModel.state.collectAsStateWithLifecycle()
        ExploreScreen(navController, exploreState, paddingValues)
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
            rememberNavController(), ExploreState(
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
    navController: NavController,
    exploreState: ExploreState,
    paddingValues: PaddingValues
) {

    // TODO: continue here
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.navigate(Screens.SearchScreen.route) }) {
                Icon(Icons.Default.Search, contentDescription = "")
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(id = R.string.find_random_user))
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.add_user_by_uid))
            }
        }
        LazyVerticalGrid(columns = GridCells.Fixed(2),modifier = Modifier.fillMaxSize()) {
            items(exploreState.users, key = {
                it.uniqueId
            }) {
                UsersCard(userInfo = it)
            }
        }
    }
}

@Composable
fun UsersCard(
    userInfo: UserInfo
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AsyncImage(
            model = userInfo.photoUri,
            contentDescription = "",
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
        )
        Text(text = userInfo.userName, modifier = Modifier.align(CenterHorizontally))
        Row(modifier = Modifier.align(CenterHorizontally)) {
            Text(text = userInfo.gender.gender)
            Icon(
                painter = painterResource(id = userInfo.gender.getGenderIcon()),
                contentDescription = "gender icon",
                tint = userInfo.gender.getSolidColorByGender()
            )
        }

    }
}