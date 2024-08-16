package com.example.chatter.chat

import android.graphics.ColorMatrix
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatter.chat.model.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ChatScreen(navController: NavController,channelId:String){
    val viewModel:ChatViewModel = hiltViewModel()
    val messages = viewModel.messages.collectAsState()
    Scaffold {
        Column (
            modifier = Modifier
                .padding(it)
                .fillMaxSize()

        ){
            LaunchedEffect(key1 = true) {
                viewModel.listenForMessages(channelId)
            }
            ChatMessages(messages = messages.value,
                onSendMessage = {
                    viewModel.SendMessage(channelId, Message(message = it))
                }
            )
        }
    }
}

@Composable
fun ChatMessages(
    messages:List<Message>,
    onSendMessage:(String)->Unit
    ){

    val msg = remember {mutableStateOf("")}

    val hideKeyboardAction = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(messages){message ->
                ChatBubble(message = message)

            }
        }
    }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        TextField(value =msg.value , onValueChange ={msg.value = it},
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(text = "Enter Your Message")
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    hideKeyboardAction?.hide()
                }
            )
        )
        IconButton(onClick = {
            onSendMessage(msg.value)
            msg.value = ""
        }) {
            Icon(imageVector = Icons.AutoMirrored.Default.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
   val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) {
        Color.Blue
    }else{
        Color.Red
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        val alignment = if (!isCurrentUser) Alignment.CenterStart else Alignment.CenterEnd
        Box (

            modifier = Modifier
                .padding(8.dp)
                .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                .align(alignment)
        ){
                Text(text = message.message, color = Color.White, modifier = Modifier.padding(8.dp))
        }

    }
}
