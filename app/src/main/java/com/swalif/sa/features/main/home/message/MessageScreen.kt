package com.swalif.sa.features.main.home.message

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.model.ChatInfo
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import com.swalif.sa.ui.theme.ChatAppTheme
import com.swalif.sa.utils.formatShortTime

fun NavGraphBuilder.messageDest(navController: NavController) {
    composable(
        Screens.MessageScreen.formattedMessageRoute, arguments = Screens.MessageScreen.args
    ) {
        MessageScreen(navController = navController)
    }
}

@Preview(showBackground = true, locale = "ar")
@Composable
fun Preview1() {
    ChatAppTheme() {

//        MessageScreen(navController = rememberNavController())
        Text(text = 0x1F3A8.toChar().toString())

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MessageScreen(
    navController: NavController,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val animateSendColor by animateColorAsState(targetValue = if (state.text.isEmpty()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary)
    val lazyColumnState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = state.messages.size) {
        lazyColumnState.animateScrollToItem(0)
    }
    val pickMedia =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                viewModel.sendImage(it.toString())
            }
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(true) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) {
        ChatInfoSection(
            navController = navController,
            chatInfo = state.chatInfo,
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(
            state = lazyColumnState,
            reverseLayout = true,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(6.dp)
        ) {
            items(state.messages, key = {
                it.messageId
            }) {
                MessageItem(
                    message = it,
                    navController
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
        TextField(
            textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Content),
            leadingIcon = {
                IconButton(onClick = {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                }) {
                    Icon(imageVector = Icons.Default.Email, "")
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.sendTextMessage(
                        state.text
                    )
                }, enabled = state.text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "",
                        tint = animateSendColor
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            value = state.text,
            onValueChange = {
                viewModel.updateMessage(it)
            })
    }

}

@Composable
fun ChatInfoSection(
    modifier: Modifier = Modifier,
    navController: NavController,
    chatInfo: ChatInfo
) {
    Row(
        modifier = modifier
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(rememberVectorPainter(image = Icons.Default.ArrowBack), "")
        }

        AsyncImage(
            model = chatInfo.imageUri,
            contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .clickable {
                    navController.navigate(Screens.PreviewScreen.navigateToPreview(chatInfo.imageUri))
                },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(text = chatInfo.userName, style = MaterialTheme.typography.titleMedium)
            Text(text = chatInfo.localizeStatusUser(), style = MaterialTheme.typography.labelSmall)
        }
    }

}

@Composable
fun getShapeMessageItemMe() = MaterialTheme.shapes.medium.copy(topEnd = CornerSize(0.dp))

@Composable
fun getShapeMessageItemSender() = MaterialTheme.shapes.medium.copy(topStart = CornerSize(0.dp))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageItem(
    message: Message,
    navController: NavController
) {
    val isMessageFromMe = remember(message.senderUid) {
        message.isMessageFromMe("test")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if (isMessageFromMe) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.width(IntrinsicSize.Max),
            shape = if (isMessageFromMe) getShapeMessageItemMe() else getShapeMessageItemSender(),
            colors = CardDefaults.cardColors(
                containerColor = if (isMessageFromMe) MaterialTheme.colorScheme.primaryContainer.copy(
                    alpha = 0.8f
                ) else MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            ),
            onClick = {
            }
        ) {
            ContentMessage(
                Modifier,
                message,
                isMessageFromMe,
                navController
            )
        }
    }
}

@Composable
fun ContentMessage(
    modifier: Modifier = Modifier,
    message: Message,
    isMessageFromMe: Boolean,
    navController: NavController
) {
    val animate =
        animateColorAsState(
            targetValue = if (message.statusMessage == MessageStatus.SEEN) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
        )
    Column {
        when (message.messageType) {
            MessageType.TEXT -> Text(
                text = message.message,
                modifier
                    .widthIn(max = 290.dp)
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                style = LocalTextStyle.current.copy(textDirection = TextDirection.Content)
            )

            MessageType.IMAGE -> {
                val imageUri = message.mediaUri
                if (imageUri != null) {
                    if (imageUri.isEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(8.dp)
                        )
                    } else {
                        AsyncImage(
                            model = message.mediaUri,
                            contentDescription = "",
                            modifier = Modifier
                                .size(150.dp)
                                .padding(2.dp)
                                .clickable {
                                    navController.navigate(
                                        Screens.PreviewScreen.navigateToPreview(
                                            message.mediaUri ?: ""
                                        )
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            MessageType.AUDIO -> TODO()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message.dateTime.formatShortTime(),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(3.dp)
            )
            if (isMessageFromMe) {
                Crossfade(targetState = message.statusMessage) {
                    when (it) {
                        MessageStatus.SENT -> {
                            Icon(
                                painter = painterResource(id = R.drawable.check_icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                        MessageStatus.DELIVERED, MessageStatus.SEEN -> {
                            Icon(
                                painter = painterResource(id = R.drawable.double_check_icon),
                                contentDescription = "",
                                tint = animate.value,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}
