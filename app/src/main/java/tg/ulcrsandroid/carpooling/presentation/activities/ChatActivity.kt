package tg.ulcrsandroid.carpooling.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.runBlocking
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.ChatService
import tg.ulcrsandroid.carpooling.application.services.DiscussionService
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.databinding.ActivityChatBinding
import tg.ulcrsandroid.carpooling.domain.models.Chat
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.presentation.adapters.ChatAdapter

class ChatActivity : AppCompatActivity() {

    private lateinit var ui: ActivityChatBinding
    private lateinit var utilisateur: Utilisateur

    override fun onCreate(savedInstanceState: Bundle?) {
        ui = ActivityChatBinding.inflate(layoutInflater)
        setContentView(ui.root)
        super.onCreate(savedInstanceState)
        // Vérifier si l'id de l'utilisateur et différent de null
//        if (UtilisateurService.utilisateurID != null) {
//            runBlocking {
//                UtilisateurService.initialiserUtilisateurSynchronement() // Récupérer les infos utilisateur et créer un objet utilisateur
//            }
//        }

//        J2jvzJuupSgRyupfk4cEzFOjh8e2 sylvaingossou
//        utilisateur = UtilisateurService.utilisateurActuel!!
//        val utilisateurList = UtilisateurService.getUsersList(utilisateur.contactsEmails)
        val adapter = ChatAdapter(createTestChats())
        adapter.onItemClick = this::onItemClick
        ui.chatRecyclerView.adapter = adapter
    }

    private fun onItemClick(idChat: String?, nomComplet: String?) {
        Log.d("Carpooling", "ChatActivity ---> UTILISATEUR SELECTIONNE ---> $nomComplet")
        val intent = Intent(this, DiscussionActivity::class.java)
        intent.putExtra("idChat", idChat)
        intent.putExtra("nomComplet", nomComplet)
        startActivity(intent)
    }

    private fun createTestUsers(): List<Utilisateur> {
        val testUsers = mutableListOf<Utilisateur>();
        for (i in 1..10) {
            val user = Utilisateur()
            user.nomComplet = "A${i} Sylvain"
            user.email = "a${i}@gmail.com"
            user.idUtilisateur = "a${i}"
            testUsers.add(user)
        }
        return testUsers
    }

    private fun createTestChats(): List<Chat> {
        val testChat = mutableListOf<Chat>();
        val utililsateurActuel = UtilisateurService.utilisateurActuel
        val chat = Chat(
            idChat = DiscussionService.generateUniqueKey(),
            nomInitialisateur = "",
            idInitialisateur = "",
            nomMembreSecondaire = "",
            idMembreSecondaire = "",
        )

        ChatService.creerRemoteChat(chat) // Ajouter le chat au remote
        // Ajouter l'id du chat à l'utilisateur secondqire
        testChat.add(chat)
        return testChat
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}