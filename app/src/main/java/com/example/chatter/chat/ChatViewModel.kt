package com.example.chatter.chat

import androidx.lifecycle.ViewModel
import com.example.chatter.chat.model.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private val db = FirebaseDatabase.getInstance()

    fun SendMessage(channelId: String, message: Message){
        val message = Message(
            id = db.reference.push().key?:UUID.randomUUID().toString(),
            message = message.message,
            senderId = Firebase.auth.currentUser?.uid?:"",
            senderName = Firebase.auth.currentUser?.displayName?:"",
            createdAt = System.currentTimeMillis()

        )
        db.reference.child("messages").child(channelId).push().setValue(message)
    }

    fun listenForMessages(channelId: String) {
        db.getReference("messages").child(channelId).orderByChild("createdAt")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    snapshot.children.forEach{
                        val message = it.getValue(Message::class.java)
                        message?.let {
                            list.add(it)
                        }
                }
                    _messages.value = list
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}