package tg.ulcrsandroid.carpooling.application.services

import android.util.Log
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import tg.ulcrsandroid.carpooling.domain.models.Discussion
import tg.ulcrsandroid.carpooling.domain.repositories.IDiscussion
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object DiscussionService : IDiscussion {

    fun creerDiscussion(chemin: String) {
        val database = Firebase.database
        val discussionRef = database.getReference(chemin)
        discussionRef.setValue(true)
    }

    /**
     * Cette fonction retourne la liste des messages sur le noeud identifier par l'id <code>s</code>
     * @param s Identifiant du Chat à dont les messages sont à retournés
     * @return List<Discussion> Liste de discussions représentant les message au noeud d'id <code>s</code>
     */
    suspend fun recupererListDeDiscussions(s: String) : MutableList<Discussion>? {
        val database = Firebase.database
        val ref = database.getReference("chats/$s/discussion")
        return retreiveMessages(ref)
    }

    suspend fun retreiveMessages(ref: DatabaseReference) : MutableList<Discussion>? {
        return suspendCoroutine { continuation ->
            ref.get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val messages = dataSnapshot.getValue<MutableList<Discussion>>()
                    continuation.resume(messages)
                } else {
                    Log.d("Carpooling", "DiscussionService:retreiveMessages ---> AUCUN MESSAGE POUR L'INSTANT")
                    continuation.resume(mutableListOf())
                }
            }.addOnFailureListener { e ->
                Log.d("Carpooling", "DiscussionService:retreiveMessages ---> ERROR ---> ${e.message}")
            }
        }
    }

    fun generateUniqueKey(): String {
        return UUID.randomUUID().toString()
    }

    override fun envoyerMessage() {
        // Implémentation de l'envoi de message
    }

    override fun recevoirMessage() {
        // Implémentation de la réception de message
    }

    override fun consulterHistoriqueMessages() {
        // Implémentation de la consultation de l'historique des messages
    }
}
