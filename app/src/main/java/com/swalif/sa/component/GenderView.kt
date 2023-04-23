@file:OptIn(ExperimentalMaterial3Api::class)

package com.swalif.sa.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swalif.sa.ui.theme.ChatAppTheme

@Preview(showBackground = true)
@Composable
fun Preview() {
    ChatAppTheme {
        var currentGender by remember {
            mutableStateOf<Gender>(Gender.MALE)
        }
        Row(modifier = Modifier) {
            GenderView(brushColor = maleGradient, gender = Gender.MALE, onSelectedGender = {
                currentGender = Gender.MALE
            }, isSelected = currentGender == Gender.MALE)
            GenderView(brushColor = femaleGradient, gender = Gender.FEMALE, onSelectedGender = {
                currentGender = Gender.FEMALE
            }, isSelected = currentGender == Gender.FEMALE)
        }
    }
}

val maleGradient: List<Color> = listOf(Color(0xFF6D6DFF), Color.Blue)
val femaleGradient: List<Color> = listOf(Color(0xFFEA76FF), Color.Magenta)

@Stable
class GenderState {
    var currentGender = mutableStateOf<Gender>(Gender.MALE)
}

@Composable
fun rememberGenderState(): GenderState {
    return remember {
        GenderState()
    }
}

@Composable
fun GenderPicker(
    genderState: GenderState,
    modifier: Modifier = Modifier,
    shouldShowError: Boolean = false
) {
    val colorError = MaterialTheme.colorScheme.error
    val shape = CardDefaults.elevatedShape

    val modifierError = remember(shouldShowError) {
        if (shouldShowError) {
            Modifier.border(1.dp, colorError, shape)
        } else {
            Modifier
        }
    }

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        GenderView(
            modifier = Modifier
                .size(80.dp)
                .then(modifierError),
            brushColor = maleGradient,
            gender = Gender.MALE,
            onSelectedGender = {
                genderState.currentGender.value = it
            },
            isSelected = genderState.currentGender.value == Gender.MALE
        )
        GenderView(
            isSelected = genderState.currentGender.value == Gender.FEMALE,
            modifier = Modifier
                .size(80.dp)
                .then(modifierError),
            brushColor = femaleGradient,
            gender = Gender.FEMALE,
            onSelectedGender = {
                genderState.currentGender.value = it
            }
        )
    }
}

@Composable
fun GenderView(
    modifier: Modifier = Modifier,
    brushColor: List<Color>,
    gender: Gender,
    isSelected: Boolean = false,
    onSelectedGender: (Gender) -> Unit,
) {
    val malePathString = stringResource(id = com.swalif.sa.R.string.male_path)
    val femalePathString = stringResource(id = com.swalif.sa.R.string.female_path)
    val genderPath = remember {
        PathParser().parsePathString(if (gender == Gender.MALE) malePathString else femalePathString)
            .toPath()
    }
    val rememberClickGender = remember {
        {
            onSelectedGender(gender)
        }
    }
    val selectionRadius = animateFloatAsState(
        targetValue = if (isSelected) 80f else 0f,
        animationSpec = tween(durationMillis = 400)
    )
    val pathBounds = remember {
        genderPath.getBounds()
    }
    ElevatedCard(onClick = rememberClickGender, modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            val scaleX = minOf(size.width / pathBounds.width, size.height / pathBounds.height)
            val translateOffset = Offset(
                x = center.x - pathBounds.width * scaleX / 2f, 0f
            )
            translate(translateOffset.x, translateOffset.y) {
                scale(scaleX, scaleX, pathBounds.topLeft) {
                    drawPath(
                        path = genderPath,
                        color = Color.LightGray
                    )
                    clipPath(
                        path = genderPath
                    ) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = brushColor,
                                radius = selectionRadius.value + 1f,
                                center = pathBounds.center
                            ),
                            radius = selectionRadius.value,
                            center = pathBounds.center
                        )
                    }

                }
            }


        }
    }
}

//@Composable
//fun GenderPicker(
//    modifier: Modifier = Modifier,
//    maleGradient: List<Color> = listOf(Color(0xFF6D6DFF), Color.Blue),
//    femaleGradient: List<Color> = listOf(Color(0xFFEA76FF), Color.Magenta),
//    distanceBetweenGenders: Dp = 50.dp,
//    pathScaleFactor: Float = 7f,
//    onGenderSelected: (Gender) -> Unit
//) {
//    var selectedGender by remember {
//        mutableStateOf<Gender>(Gender.Female)
//    }
//    var center by remember {
//        mutableStateOf(Offset.Unspecified)
//    }
//
//    // Get the SVG raw paths
//    val malePathString = stringResource(id = com.swalif.sa.R.string.male_path)
//    val femalePathString = stringResource(id = com.swalif.sa.R.string.female_path)
//
//    // Parse the SVG to Compose Path
//    val malePath = remember {
//        PathParser().parsePathString(malePathString).toPath()
//    }
//    val femalePath = remember {
//        PathParser().parsePathString(femalePathString).toPath()
//    }
//
//    // Get bounding rectangle of the paths
//    val malePathBounds = remember {
//        malePath.getBounds()
//    }
//    val femalePathBounds = remember {
//        femalePath.getBounds()
//    }
//
////    var maleTranslationOffset by remember {
////        mutableStateOf(Offset.Zero)
////    }
////    var femaleTranslationOffset by remember {
////        mutableStateOf(Offset.Zero)
////    }
////
////    var currentClickOffset by remember {
////        mutableStateOf(Offset.Zero)
////    }
//
//    val maleSelectionRadius = animateFloatAsState(
//        targetValue = if(selectedGender is Gender.Male) 80f else 0f,
//        animationSpec = tween(durationMillis = 1500)
//    )
//    val femaleSelectionRadius = animateFloatAsState(
//        targetValue = if(selectedGender is Gender.Female) 80f else 0f,
//        animationSpec = tween(durationMillis = 1500)
//    )
//
//    Canvas(
//        modifier = modifier
//            .pointerInput(true) {
//                detectTapGestures {
//                    val transformedMaleRect = Rect(
//                        offset = maleTranslationOffset,
//                        size = malePathBounds.size * pathScaleFactor
//                    )
//                    val transformedFemaleRect = Rect(
//                        offset = femaleTranslationOffset,
//                        size = femalePathBounds.size * pathScaleFactor
//                    )
//
//                    if(selectedGender !is Gender.Male && transformedMaleRect.contains(it)) {
//                        currentClickOffset = it
//                        selectedGender = Gender.Male
//                        onGenderSelected(Gender.Male)
//                    } else if(selectedGender !is Gender.Female && transformedFemaleRect.contains(it)) {
//                        currentClickOffset = it
//                        selectedGender = Gender.Female
//                        onGenderSelected(Gender.Female)
//                    }
//                }
//            }
//    ) {
//        center = this.center
//
//        maleTranslationOffset = Offset(
//            x = center.x - malePathBounds.width * pathScaleFactor - distanceBetweenGenders.toPx() / 2f,
//            y = center.y - pathScaleFactor * malePathBounds.height / 2f
//        )
//        femaleTranslationOffset = Offset(
//            x = center.x + distanceBetweenGenders.toPx() / 2f,
//            y = center.y - pathScaleFactor * femalePathBounds.height / 2f
//        )
//
//        // Calculate the center for the male & female paths
//        val untransformedMaleClickOffset = if(currentClickOffset == Offset.Zero) {
//            malePathBounds.center
//        } else {
//            (currentClickOffset - maleTranslationOffset) / pathScaleFactor
//        }
//        val untransformedFemaleClickOffset = if(currentClickOffset == Offset.Zero) {
//            femalePathBounds.center
//        } else {
//            (currentClickOffset - femaleTranslationOffset) / pathScaleFactor
//        }
//
//        // MALE : move to center of screen
//        translate(
//            left = maleTranslationOffset.x,
//            top = maleTranslationOffset.y
//        ) {
//            // Scale up
//            scale(
//                scale = pathScaleFactor,
//                pivot = malePathBounds.topLeft  // Scale from top left (anchor point)
//            ) {
//                drawPath(
//                    path = malePath,
//                    color = Color.LightGray
//                )
//
//                // Show the "shimmer" effect
//                clipPath(
//                    path = malePath
//                ) {
//                    drawCircle(
//                        brush = Brush.radialGradient(
//                            colors = maleGradient,
//                            center = untransformedMaleClickOffset,
//                            radius = maleSelectionRadius.value + 1f
//                        ),
//                        radius = maleSelectionRadius.value,
//                        center = untransformedMaleClickOffset
//                    )
//                }
//            }
//
//        }
//
//        // FEMALE: move to center of screen
//        translate(
//            left = femaleTranslationOffset.x,
//            top = femaleTranslationOffset.y
//        ) {
//            // Scale up
//            scale(
//                scale = pathScaleFactor,
//                pivot = femalePathBounds.topLeft
//            ) {
//                drawPath(
//                    path = femalePath,
//                    color = Color.LightGray
//                )
//
//                // Show the "shimmer" effect
//                clipPath(
//                    path = femalePath
//                ) {
//                    drawCircle(
//                        brush = Brush.radialGradient(
//                            colors = femaleGradient,
//                            center = untransformedFemaleClickOffset,
//                            radius = femaleSelectionRadius.value + 1
//                        ),
//                        radius = femaleSelectionRadius.value,
//                        center = untransformedFemaleClickOffset
//                    )
//                }
//            }
//        }
//
//
//    }
//}
enum class Gender(val gender: String) {
    MALE("Male"), FEMALE("Female");

    @Composable
    fun getColorByGender(): Brush {
        return when (this) {
            FEMALE -> Brush.linearGradient(colors = femaleGradient)
            MALE -> Brush.linearGradient(colors = maleGradient)
        }

    }
}