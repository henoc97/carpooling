package tg.ulcrsandroid.carpooling.application.services

import com.google.firebase.Firebase
import com.google.firebase.database.database
import tg.ulcrsandroid.carpooling.domain.models.Chat

object ChatService {

    fun creerRemoteChat(chat: Chat) {
        val database = Firebase.database
        val chatRef = database.getReference("chats/${chat.idChat}")
        chatRef.setValue(chat)
    }
}