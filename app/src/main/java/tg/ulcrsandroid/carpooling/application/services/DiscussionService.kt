package tg.ulcrsandroid.carpooling.application.services

import com.google.firebase.Firebase
import com.google.firebase.database.database
import tg.ulcrsandroid.carpooling.domain.models.Discussion
import tg.ulcrsandroid.carpooling.domain.repositories.IDiscussion
import java.util.UUID

object DiscussionService : IDiscussion {

    fun creerDiscussion(chemin: String) {
        val database = Firebase.database
        val discussionRef = database.getReference(chemin)
        discussionRef.setValue(true)
    }

    fun recupererListDeDiscussions() : List<Discussion> {
        val l = listOf<Discussion>()
        return l
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
