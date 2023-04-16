package com.swalif.sa.features.main.explore.search

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.ui.theme.ChatAppTheme
import java.lang.Float.max
import kotlin.math.PI
import kotlin.math.abs

fun NavGraphBuilder.searchScreen(navController: NavController) {
    composable(Screens.SearchScreen.route) {
        SearchScreen(navController)
    }
}

@Preview
@Composable
fun Preview2() {
    ChatAppTheme {

        ImageProgressIndicator()
    }
}

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchState by searchViewModel.searchState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        ElevatedCard(
            modifier = Modifier
                .align(Alignment.Center)
                .size(200.dp)
        ) {
//            AsyncImage(model = Icons.Default.Email, contentDescription = "",modifier = Modifier.drawBehind {
//                drawArc(
//                    Color.Green,0f,260f,false,style = Stroke(2f)
//                )
//            })

            ImageProgressIndicator()
        }
    }
}

@Composable
fun ImageProgressIndicator() {
    val stroke = with(LocalDensity.current) {
        Stroke(2.dp.toPx(),cap = StrokeCap.Square)
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
        tween(1332,easing = LinearEasing)
        )
    )
    val endAngle = transition.animateFloat(initialValue = 0f, targetValue = 290f, animationSpec = infiniteRepeatable(
        keyframes {
            durationMillis = (1332 * 0.5).toInt() + (1332 * 0.5).toInt()
            0f at 0 with CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
            290f at (1332 * 0.5).toInt()
        }
    ))
    val startAngle = transition.animateFloat(initialValue = 0f, targetValue = 290f, animationSpec = infiniteRepeatable(
        keyframes {
            durationMillis = (1332 * 0.5).toInt() + (1332 * 0.5).toInt()
            0f at (1332 * 0.5).toInt() with CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
            290f at durationMillis
        }
    ))
    val color =MaterialTheme.colorScheme.primary
    val icon = painterResource(id = R.drawable.user_icon)
    val getOffsetIcon = icon.intrinsicSize
    Icon(
        painter = painterResource(id = R.drawable.user_icon),
        contentDescription = "",
        modifier = Modifier
            .size(50.dp)
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
            })
}

fun DrawScope.drawIndeterminateProgress(
    startAngle: Float,
    strokeWidth: Dp,
    sweep: Float,
    color: Color,
    stroke: Stroke,
    offset: Size
) {
    val strokeCapOffset = if (stroke.cap == StrokeCap.Butt){
        0f
    }else {
        (180.0 / PI).toFloat() * (strokeWidth / (CircularIndicatorDiameter / 2)) / 2f
    }
    val adjustedStartAngle = startAngle + strokeCapOffset
    val adjustedSweep = max (sweep,0.1f)

    drawCircularProgress (adjustedStartAngle,adjustedSweep,color,stroke,offset)
}

fun DrawScope.drawCircularProgress(
    adjustedStartAngle: Float,
    adjustedSweep: Float,
    color: Color,
    stroke: Stroke,
    offset: Size
) {

    val diameterOffset = stroke.width/2
    val arcDimen = size.width - 2 * diameterOffset
    val offsetI = Offset(center.x - offset.width,center.y-offset.width)

    // TODO: fix this
    drawArc(
        color = color ,
        startAngle = adjustedStartAngle,
        sweepAngle = adjustedSweep,
        useCenter = false,
        topLeft =Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen,arcDimen),
        style = stroke
    )
}
internal val CircularIndicatorDiameter =
    48.dp - 4.dp * 2
