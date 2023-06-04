package com.swalif.sa.features.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.swalif.sa.Screens
import com.swalif.sa.model.Chat
import com.swalif.sa.ui.theme.ChatAppTheme
import com.swalif.sa.utils.formatSortTime
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

fun NavGraphBuilder.homeDest(
    navController: NavController,
    paddingValues: PaddingValues,
    nestedScroll: NestedScrollConnection
) {
    composable(Screens.MainScreens.HomeScreen.route) {
        HomeScreen(navController, paddingValues, nestedScroll)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    nestedScrollConnection: NestedScrollConnection,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by viewModel.homeState.collectAsState()

    val rememberOnDelete = remember {
        { chatId: String ->
            viewModel.deleteChatById(chatId)
        }
    }
    val rememberOnNavigate = remember {
        { chatId: String ,isSave:Boolean->
            navController.navigate(
                Screens.MessageScreen.navigateToMessageScreen(
                    homeState.myUid!!,
                    chatId,
                    isSave
                )
            )
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .nestedScroll(nestedScrollConnection),
    ) {

        items(homeState.tempChats,key = {it.chatId}){
            ChatItem(chat = it, rememberOnDelete, rememberOnNavigate)
            Divider()
        }
        items(homeState.chats, key = {
            it.chatId
        }) {
            ChatItem(chat = it, rememberOnDelete, rememberOnNavigate)
            Divider()
        }
    }
}


@Preview
@Composable
fun Preview1() {
    ChatAppTheme {
        ChatItem(
            chat = Chat(
                "",
                "",
                "",
                "Hi",
                "https://i.pinimg.com/originals/b4/c1/fb/b4c1fbf0e913bf9365c8fa0dcc48c0c0.jpg",
                0,
                10,
                "", 5
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatItem(
    chat: Chat,
    onDelete: (ChatId: String) -> Unit = {},
    onNavigate: (String,Boolean) -> Unit = {id,isSave->}
) {
    val deleteAction = SwipeAction(
        onSwipe = {
            onDelete(chat.chatId)
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(16.dp)
            )
        },
        background = MaterialTheme.colorScheme.errorContainer
    )
    SwipeableActionsBox(
        endActions = listOf(deleteAction)
    ) {
        ElevatedCard(onClick = {
            onNavigate(chat.chatId,chat.isSaveLocally)

        }, shape = RoundedCornerShape(0.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AsyncImage(
                        model = chat.imageUri,
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = chat.senderName, style = MaterialTheme.typography.titleMedium)
                        Text(text = chat.lastMessage, style = MaterialTheme.typography.labelSmall)
                    }

                }
                Column(
                    modifier = Modifier.align(
                        Alignment.CenterEnd
                    ),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = chat.lastMessageDate.formatSortTime(),
                        style = MaterialTheme.typography.labelSmall,
                    )
                    if (chat.messagesUnread > 0) {
                        Box(
                            modifier = Modifier
                                .defaultMinSize(minWidth = 24.dp, minHeight = 24.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape
                                )
                                .clip(CircleShape)
                                .padding(horizontal = 4.dp)
                                .align(CenterHorizontally)
                        ) {
                            Text(
                                text = if (chat.messagesUnread >= 99) "99+" else chat.messagesUnread.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.align(Center)
                            )
                        }
                    }
                }
            }
        }
    }
}