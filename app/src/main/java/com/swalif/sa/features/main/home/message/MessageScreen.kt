package com.swalif.sa.features.main.home.message

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDirection
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
import com.google.firebase.Timestamp
import com.swalif.sa.R
import com.swalif.sa.Screens
import com.swalif.sa.component.AdmobComposable
import com.swalif.sa.model.ChatInfo
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import com.swalif.sa.model.RequestFriendStatus
import com.swalif.sa.model.UserStatus
import com.swalif.sa.ui.theme.ChatAppTheme
import com.swalif.sa.utils.formatSortTime

fun NavGraphBuilder.messageDest(navController: NavController) {
    composable(
        Screens.MessageScreen.formattedMessageRoute, arguments = Screens.MessageScreen.args
    ) {
        MessageScreen(navController = navController)
    }
}

@Preview(
    showBackground = true, locale = "ar",
    device = "spec:width=1080px,height=2340px,dpi=440",
    wallpaper = Wallpapers.NONE,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED
)
@Composable
fun AnnouncementPreview() {
    ChatAppTheme() {
        AnnouncementContent(content = "salman sent friend request")
    }
}

@Preview(
    showBackground = true, locale = "ar",
    device = "spec:width=1080px,height=2340px,dpi=440",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED
)
@Composable
fun Preview1() {
    ChatAppTheme() {
//        MessageItem(
//            message = Message(
//                0,
//                "",
//                "",
//                "salman",
//                Timestamp.now().toDate().time,
//                null,
//                MessageStatus.DELIVERED,
//                MessageType.TEXT
//            ), rememberNavController(), ""
//        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MessageScreen(
    navController: NavController,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val lazyColumnState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val messageEvent by viewModel.messageEventDataChange.observeAsState()
    val friendEvent by viewModel.friendEvent.collectAsState()
    val context = LocalContext.current
    val receiverUser = state.chatInfo.userName
    val messageToast = stringResource(id = R.string.acceptedFriend, receiverUser)
    val showDialog by viewModel.showDialogLeaveUser.collectAsStateWithLifecycle()
    val leaveChat by viewModel.leaveChat.observeAsState()

    if (showDialog) {
        AlertDialog(title = {
            Text(text = stringResource(id = R.string.sure))
        }, onDismissRequest = { viewModel.dialogEvent(false) }, confirmButton = {
            TextButton(onClick = {
                viewModel.leaveChat()
                viewModel.dialogEvent(false)
            }) {
                Text(text = stringResource(id = R.string.confirm))
            }
        }, dismissButton = {
            TextButton(onClick = {
                viewModel.dialogEvent(false)
            }) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        })
    }

    LaunchedEffect(key1 = leaveChat) {
        if (leaveChat == true) {
            navController.popBackStack()
        }
    }
    LaunchedEffect(key1 = friendEvent) {
        friendEvent?.let {
            Toast.makeText(context, messageToast, Toast.LENGTH_SHORT).show()
            viewModel.restRequest()
        }
    }
    BackHandler {
        if (state.chatInfo.requestFriendStatus != RequestFriendStatus.ACCEPTED) {
            viewModel.dialogEvent(true)
        } else if (state.chatInfo.userIsLeft) {
            navController.popBackStack()
            Toast.makeText(context, "user has left", Toast.LENGTH_SHORT).show()
            viewModel.leaveChat()
        } else {
            navController.popBackStack()
        }
    }
    LaunchedEffect(key1 = messageEvent) {
        messageEvent?.let {
            lazyColumnState.animateScrollToItem(0)
            viewModel.restMessageEvent()
        }
    }

    val rememberPreviewScreen = remember {
        { image: String ->
            navController.navigate(Screens.PreviewScreen.navigateToPreview(image))
        }
    }
    val rememberOnImageClick = remember {
        { image: String ->
            navController.navigate(
                Screens.PreviewScreen.navigateToPreview(
                    image
                )
            )
        }
    }
    val rememberOnBack = remember<() -> Unit> {
        {
            if (state.chatInfo.requestFriendStatus != RequestFriendStatus.ACCEPTED) {
                viewModel.dialogEvent(true)
            } else if (state.chatInfo.userIsLeft) {
                navController.popBackStack()
                viewModel.leaveChat()
                Toast.makeText(context, "user has left", Toast.LENGTH_SHORT).show()

            } else {
                navController.popBackStack()
            }
        }
    }
    val rememberOnClickRequestFriend = remember{
        {
            viewModel.updateFriendRequest()
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
            .navigationBarsPadding()
    ) {

        ChatInfoSection(
            onClickImage = rememberPreviewScreen,
//            chatInfo = {state.chatInfo},
            modifier = Modifier.fillMaxWidth(),
            onRequestFriendClick = rememberOnClickRequestFriend,
            onBack = rememberOnBack,
            userStatus = state.chatInfo.localizeStatusUser(),
            username = state.chatInfo.userName,
            image = state.chatInfo.imageUri,
            friendRequest = state.chatInfo.requestFriendStatus
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
            }, contentType = { it.messageType }) {
                if (it.messageType == MessageType.ANNOUNCEMENT) {
                    if (it.senderUid != state.myUid) {
                        AnnouncementContent(content = it.message)
                    }
                } else {
                    MessageItem(
                        it.isMessageFromMe(state.myUid),
//                        navController,
                        it.statusMessage,
                        it.messageType,
                        it.message, it.mediaUri, it.dateTime.formatSortTime(), rememberOnImageClick
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                }
            }

        }

        EditTextField(
            onSendMessage = viewModel::sendTextMessage,
            onSendImage = viewModel::sendImage,
            viewModel::updateTypingUser
        )
    }
}


@Composable
fun EditTextField(
    onSendMessage: (String) -> Unit,
    onSendImage: (String) -> Unit,
    onValueChange: () -> Unit
) {
    var message by remember {
        mutableStateOf("")
    }
    val animateSendColor by animateColorAsState(targetValue = if (message.isEmpty()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary)
    val pickMedia =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                onSendImage(it.toString())
            }
        }
    OutlinedTextField(
        shape = CircleShape,
        textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Content),
        leadingIcon = {
            IconButton(onClick = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Icon(painter= painterResource(id = R.drawable.image_icon), "email",modifier = Modifier.size(24.dp))
            }
        },
        trailingIcon = {
            IconButton(onClick = {
                onSendMessage(message)
                message = ""
            }, enabled = message.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "",
                    tint = animateSendColor
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .imePadding(),
        value = message,
        onValueChange = {
            message = it
            onValueChange()
        })
}

@Composable
fun ChatInfoSection(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onClickImage: (String) -> Unit,
//    chatInfo:()-> ChatInfo,
    image: String,
    username: String,
    userStatus: String,
    friendRequest: RequestFriendStatus,
    onRequestFriendClick: () -> Unit
) {
    val iconFriend = remember(friendRequest) {
        when (friendRequest) {
            RequestFriendStatus.IDLE -> R.drawable.request_friend_icon
            RequestFriendStatus.SENT -> R.drawable.request_friend_pending_icon
            RequestFriendStatus.ACCEPTED -> R.drawable.check_icon
        }
    }


    Box(
        modifier = modifier
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
            .statusBarsPadding()
            .padding(bottom = 6.dp),
        contentAlignment = CenterStart
    ) {

        Column(modifier = Modifier.wrapContentSize()) {
            AdmobComposable()
            Row(modifier = Modifier.wrapContentSize()) {
                IconButton(onClick = onBack) {
                    Icon(rememberVectorPainter(image = Icons.Default.ArrowBack), "")
                }

                AsyncImage(
                    model = image,
                    contentDescription = "",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .clickable {
                            onClickImage(image)
                        },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = username, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = userStatus,
                        style = MaterialTheme.typography.labelSmall
                    )

                }
            }
        }
        if (friendRequest != RequestFriendStatus.ACCEPTED) {
            IconButton(
                modifier = Modifier.align(BottomEnd), onClick = onRequestFriendClick
            ) {
                Icon(
                    modifier = Modifier,
                    painter = painterResource(
                        iconFriend
                    ), contentDescription = "Request Friend"
                )
            }
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
    isMessageFromMe: Boolean,
    statusMessage: MessageStatus,
    messageType: MessageType,
    content: String,
    imageUri: String? = null,
    timeStamp: String,
    onImageClick: (String) -> Unit
) {


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

            ) {
            ContentMessage(
                Modifier,
                isMessageFromMe,
//                navController,
                onImageClick,
                statusMessage, messageType, content, imageUri, timeStamp
            )
        }
    }
}

@Composable
fun ContentMessage(
    modifier: Modifier = Modifier,
    isMessageFromMe: Boolean,
    onImageClick: (String) -> Unit,
    statusMessage: MessageStatus,
    messageType: MessageType,
    content: String,
    imageUri: String? = null,
    timeStamp: String,
) {
    val animate =
        animateColorAsState(
            targetValue = if (statusMessage == MessageStatus.SEEN) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
        )
    val rememberIconMessage = remember(statusMessage) {
        when (statusMessage) {
            MessageStatus.LOADING -> {
                R.drawable.loading_message_icon
            }

            MessageStatus.SENT -> {
                R.drawable.check_icon
            }

            MessageStatus.DELIVERED -> {
                R.drawable.double_check_icon
            }

            MessageStatus.SEEN -> {
                R.drawable.double_check_icon
            }
        }
    }
    Column {
        when (messageType) {
            MessageType.TEXT -> Text(
                text = content,
                modifier
                    .widthIn(max = 290.dp)
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp, top = 3.dp),
                style = LocalTextStyle.current.copy(textDirection = TextDirection.Content)
            )

            MessageType.IMAGE -> {

                if (imageUri != null) {
                    if (imageUri.isEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(8.dp)
                        )
                    } else {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "",
                            modifier = Modifier
                                .size(150.dp)
                                .clickable {
                                    onImageClick(imageUri)
                                },
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }

            MessageType.AUDIO -> TODO()
            MessageType.ANNOUNCEMENT -> {

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = timeStamp,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 3.dp)
            )
            if (isMessageFromMe) {
                Icon(
                    painter = painterResource(id = rememberIconMessage),
                    contentDescription = "",
                    tint = animate.value,
                    modifier = Modifier.size(
                        20.dp
                    )
                )

            }
        }
    }
}

@Composable
fun AnnouncementContent(content: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(2.dp)
                .align(Center)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.request_friend_icon),
                    contentDescription = "request friend",
                    modifier = Modifier
                        .size(26.dp)
                        .padding(horizontal = 4.dp)
                )
                Text(
                    text = content,
                    modifier = Modifier.padding(6.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
