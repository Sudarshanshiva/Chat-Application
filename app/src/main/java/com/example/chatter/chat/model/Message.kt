package com.example.chatter.chat.model

data class Message(
    val id: String = "",
    val message: String = "",
    val senderId: String = "",
    val senderName:String = "",
    val createdAt: Long = System.currentTimeMillis()
)
