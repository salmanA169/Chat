package com.swalif.sa.features.main.explore.search

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.core.searchManager.RoomEvent
import com.swalif.sa.core.searchManager.RoomStatus
import com.swalif.sa.core.searchManager.UserState
import com.swalif.sa.model.UserInfo
import com.swalif.sa.ui.theme.ChatAppTheme
import logcat.logcat
import java.lang.Float.max
import kotlin.math.PI
import kotlin.math.abs
import kotlin.random.Random

fun NavGraphBuilder.searchScreen(navController: NavController) {
    composable(Screens.SearchScreen.route) {
        SearchScreen(navController)
    }
}

@Preview(group = "s1", wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Preview(group = "s1", wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun Preview2() {
    ChatAppTheme {
        SearchScreen(navController = rememberNavController())
    }
}

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchState by searchViewModel.searchState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = searchState.roomEvent.startChatRoom) {
        if (searchState.roomEvent.startChatRoom){
            val myCurrentUser = searchState.myCurrentUser!!
            val getUser = searchState.roomEvent.users.find { it.userInfo != myCurrentUser }!!.userInfo
            val chatId =Random.nextInt()
            searchViewModel.addTestChat(chatId ,getUser.userName,getUser.photoUri,getUser.uidUser)
            navController.navigate(Screens.MessageScreen.navigateToMessageScreen("test",chatId.toString())){
                popUpTo(Screens.MainScreens.HomeScreen.route)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        ElevatedCard(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.7f)
        ) {
            UserStatusSection(
                Modifier.align(CenterHorizontally),
                searchState.roomEvent,
                searchState.myCurrentUser,
                searchViewModel::updateUserStatus
            )
        }
    }
}

@Composable
fun UserStatusSection(
    modifier: Modifier = Modifier,
    roomEvent: RoomEvent,
    myCurrentUserInfo: UserInfo? = null,
    onUserStateChange: (UserState) -> Unit
) {
    when (roomEvent.roomStatus) {
        RoomStatus.WAITING_USERS -> {
            ImageProgressIndicator(
                modifier
                    .padding(vertical = 8.dp)
            )
            Text(
                text = stringResource(id = R.string.find_users),
                modifier = modifier.padding(vertical = 8.dp)
            )
        }
        RoomStatus.COMPLETE_USERS -> {
            val getUser = roomEvent.users.find { it.userInfo != myCurrentUserInfo }!!
            val painter = ImageRequest.Builder(LocalContext.current)
                .transformations(listOf(CircleCropTransformation())).data(getUser.userInfo.photoUri)
                .build()
            AsyncImage(
                model = painter, contentDescription = "", modifier = modifier
                    .padding(top = 8.dp)
                    .size(80.dp)
                    .border(
                        1.dp, getUser.userInfo.gender.getColorByGender(),
                        CircleShape
                    )
            )
            Text(
                text = if (getUser.isLeft()) stringResource(id = R.string.user_left) else if (getUser.isAccept()) getUser.userInfo.userName.plus(
                    " - accepted"
                ) else getUser.userInfo.userName,
                modifier = modifier.padding(vertical = 6.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    onClick = { onUserStateChange(UserState.IGNORE) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = stringResource(id = R.string.ignore),
                        color = if (!getUser.isLeft()) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    enabled = !getUser.isLeft(), modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 6.dp), onClick = {
                        onUserStateChange(UserState.ACCEPT)
                    }) {
                    Text(text = stringResource(id = R.string.accept))
                }
            }
        }
    }

}

@Composable
fun ImageProgressIndicator(modifier: Modifier = Modifier) {
    val stroke = with(LocalDensity.current) {
        Stroke(2.dp.toPx(), cap = StrokeCap.Square)
    }
    val transition = rememberInfiniteTransition()
    val currentRotation = transition.animateValue(
        initialValue = 0,
        targetValue = 5,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                1332 * 5, easing = LinearEasing
            )
        )
    )
    val baseRotation = transition.animateFloat(
        initialValue = 0f, targetValue = 286f, animationSpec = infiniteRepeatable(
            tween(1332, easing = LinearEasing)
        )
    )
    val endAngle = transition.animateFloat(initialValue = 0f,
        targetValue = 290f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = (1332 * 0.5).toInt() + (1332 * 0.5).toInt()
                0f at 0 with CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
                290f at (1332 * 0.5).toInt()
            }
        ))
    val startAngle = transition.animateFloat(initialValue = 0f,
        targetValue = 290f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = (1332 * 0.5).toInt() + (1332 * 0.5).toInt()
                0f at (1332 * 0.5).toInt() with CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
                290f at durationMillis
            }
        ))
    val color = MaterialTheme.colorScheme.primary
    val icon = painterResource(id = R.drawable.user_icon)
    val getOffsetIcon = icon.intrinsicSize

    Icon(
        painter = painterResource(id = R.drawable.user_icon),
        contentDescription = "",
        modifier = modifier
            .drawBehind {
                val currentRotationOffset = (currentRotation.value * ((286 + 290)) % 360)
                val sweep = abs(endAngle.value - startAngle.value)
                val offset = -90f + currentRotationOffset + baseRotation.value
                drawIndeterminateProgress(
                    startAngle.value + offset,
                    2.dp,
                    sweep,
                    color,
                    stroke,
                    getOffsetIcon
                )
            }
            .size(80.dp)
    )
}

fun DrawScope.drawIndeterminateProgress(
    startAngle: Float,
    strokeWidth: Dp,
    sweep: Float,
    color: Color,
    stroke: Stroke,
    offset: Size
) {
    val strokeCapOffset = if (stroke.cap == StrokeCap.Butt) {
        0f
    } else {
        (180.0 / PI).toFloat() * (strokeWidth / (CircularIndicatorDiameter / 2)) / 2f
    }
    val adjustedStartAngle = startAngle + strokeCapOffset
    val adjustedSweep = max(sweep, 0.1f)

    drawCircularProgress(adjustedStartAngle, adjustedSweep, color, stroke, offset)
}

fun DrawScope.drawCircularProgress(
    adjustedStartAngle: Float,
    adjustedSweep: Float,
    color: Color,
    stroke: Stroke,
    offset: Size
) {

    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset

    drawArc(
        color = color,
        startAngle = adjustedStartAngle,
        sweepAngle = adjustedSweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

internal val CircularIndicatorDiameter =
    48.dp - 4.dp * 2
