package com.swalif.sa.features.main.home.message

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
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
import com.swalif.sa.model.UserStatus
import com.swalif.sa.ui.theme.ChatAppTheme

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
        MessageScreen(navController = rememberNavController())
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
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ChatInfoSection(navController = navController, chatInfo = state.chatInfo,modifier = Modifier.fillMaxWidth())
        LazyColumn(
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
                    message = it
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
        TextField(
            textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Content),
            leadingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Email, "")
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.sendMessage(
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
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(text = chatInfo.userName, style = MaterialTheme.typography.labelLarge)
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
                Modifier.padding(6.dp),
                message.message,
                message.statusMessage,
                isMessageFromMe
            )
        }
    }
}

@Composable
fun ContentMessage(
    modifier: Modifier = Modifier,
    text: String,
    messageStatus: MessageStatus,
    isMessageFromMe: Boolean
) {
    val animate =
        animateColorAsState(
            targetValue = if (messageStatus == MessageStatus.SEEN) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
        )
    Column() {
        Text(
            text = text,
            modifier
                .widthIn(max = 290.dp)
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            style = LocalTextStyle.current.copy(textDirection = TextDirection.Content)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "08:53", style = MaterialTheme.typography.labelSmall)
            if (isMessageFromMe) {
                Crossfade(targetState = messageStatus) {
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
