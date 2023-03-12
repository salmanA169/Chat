package com.swalif.sa.features.main.home.message

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
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
    LocalViewModelStoreOwner
    val state by viewModel.state.collectAsState()
    val animateSendColor by animateColorAsState(targetValue = if (state.text.isEmpty()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        AsyncImage(
                            model = state.chatInfo.imageUri,
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(text = state.chatInfo.userName)
                        val userStatus = state.chatInfo.userStatus
                        when(userStatus){
                            is UserStatus.Offline -> {
                                Text(text = userStatus.lastSeen.toString())
                            }
                            UserStatus.Online -> {
                                Text(text = "Onling")
                            }
                            UserStatus.TYPING -> {
                                Text(text = "Typing")
                            }
                            null -> {

                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {

                        Icon(Icons.Default.ArrowBack,"")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
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
                containerColor = if (isMessageFromMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            ),
            onClick = {
            }
        ) {
            ContentMessage(Modifier.padding(6.dp), message.message, message.statusMessage)
        }
    }
}

@Composable
fun ContentMessage(
    modifier: Modifier = Modifier,
    text: String,
    messageStatus: MessageStatus
) {
    val animate =
        animateColorAsState(
            targetValue = if (messageStatus == MessageStatus.SEEN) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onBackground
        )
    Column() {
        Text(
            text = text,
            modifier.widthIn(max = 290.dp),
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
