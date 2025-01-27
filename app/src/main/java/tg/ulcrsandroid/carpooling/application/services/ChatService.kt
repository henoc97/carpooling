package tg.ulcrsandroid.carpooling.application.services

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.asDeferred
import okhttp3.internal.wait
import tg.ulcrsandroid.carpooling.domain.models.Chat
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ChatService {

    fun creerRemoteChat(chat: Chat) {
        val database = Firebase.database
        val chatRef = database.getReference("chats/${chat.idChat}")
        chatRef.setValue(chat)
    }

    suspend fun getChatsByIds(l: List<String>): MutableList<Chat?> {
        val database = Firebase.database
        val chats: MutableList<Chat?> = mutableListOf()
        l.forEach { s ->
            Log.d("Carpooling", "ChatService:getChatsByIds ---> RETREIVING DATA FROM ---> chats/$s")
            val ref = database.getReference("chats/$s")
            chats.add(retreiveChat(ref))
        }
        Log.d("Carpooling", "ChatService:getChatsByIds ---> CHATS ---> $chats")
        return chats
    }

    suspend fun retreiveChat(ref: DatabaseReference): Chat? {
        return suspendCoroutine { continuation ->
            ref.get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val chat = dataSnapshot.getValue<Chat>()
                    continuation.resume(chat)
                } else {
                    Log.d("Carpooling", "ChatService:retreiveChat ---> L'UTILISATEUR N'EXISTE PAS")
                }
            }
        }
    }
}