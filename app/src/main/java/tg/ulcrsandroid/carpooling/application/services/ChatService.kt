package tg.ulcrsandroid.carpooling.application.services

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.asDeferred
import okhttp3.internal.wait
import tg.ulcrsandroid.carpooling.application.utils.UserManager
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

//    suspend fun recupererIdsFcmTokens(idChat: String) {
//        val tokens = mutableListOf<String>()
//        val ref = Firebase.database.reference.child("chats/$idChat/idInitialisateur")
//        ref.get().addOnSuccessListener { snapshot ->
//            val userId1 = snapshot.getValue<String>()
//            UtilisateurService.getFcmTokenById(userId1!!)
//        }
//    }

    suspend fun findCommonChat(l: List<String>, idSecondMembre: String) : Chat{
        val database = Firebase.database
        l.forEach { s ->
            val ref = database.getReference("chats/$s")
            val chat = retreiveChat(ref)
            if (chat?.idInitialisateur == idSecondMembre || chat?.idMembreSecondaire == idSecondMembre) {
                return chat
            }
        }
        Log.d("Carpooling", "ChatService:findCommonChat ---> NO COMMON CHAT FOUND")
        val chat = Chat()
        val ref = database.getReference("chats").push()
        // Recuperer le conducteur
        val secondMembre = UtilisateurService.getUtilisateurById(idSecondMembre)
        chat.idChat = ref.key!!
        chat.idInitialisateur = UserManager.getCurrentUser()!!.idUtilisateur
        chat.idMembreSecondaire = idSecondMembre
        chat.nomInitialisateur = UserManager.getCurrentUser()!!.nomComplet
        chat.nomMembreSecondaire = secondMembre!!.nomComplet
        creerRemoteChat(chat)

        // Ajouter l'id du chat aux deux users
        UserManager.getCurrentUser()!!.mesChats.add(chat.idChat)
        UtilisateurService.mettreAJourProfil(UserManager.getCurrentUser()!!)
        secondMembre.mesChats.add(chat.idChat)
        UtilisateurService.mettreAJourProfil(secondMembre)
        return chat
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