package com.swalif.sa.features.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.swalif.sa.Screens
import com.swalif.sa.model.Chat
import com.swalif.sa.ui.theme.ChatAppTheme
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .nestedScroll(nestedScrollConnection),
    ) {
        item {

            Button(onClick = {
                viewModel.addTestChat(
                    Chat(
                        0,
                        "salman",
                        "saleh",
                        "salman alamoudi",
                        "كيف حالك",
                        "https://i.pinimg.com/originals/b4/c1/fb/b4c1fbf0e913bf9365c8fa0dcc48c0c0.jpg",
                        LocalDateTime.now(),
                        2
                    )
                )
            }) {

            }
        }
        items(homeState.chats, key = {
            it.chatId
        }) {
            ChatItem(chat = it,navController)
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
                1,
                "",
                "",
                "salman alamoudi",
                "Hi",
                "https://i.pinimg.com/originals/b4/c1/fb/b4c1fbf0e913bf9365c8fa0dcc48c0c0.jpg",
                LocalDateTime.now(),
                10
            ), rememberNavController()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatItem(
    chat: Chat,
    navController: NavController
) {

    ElevatedCard(onClick = {
        navController.navigate(Screens.MessageScreen.navigateToMessageScreen("test","${chat.chatId}"))
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
                    text = chat.lastMessageDate.format(DateTimeFormatter.ISO_TIME),
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