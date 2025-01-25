package tg.ulcrsandroid.carpooling.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
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
    // Création d'objets de test
    private lateinit var sender: Utilisateur
    private lateinit var receiver: Utilisateur

    override fun onCreate(savedInstanceState: Bundle?) {
        ui = ActivityChatBinding.inflate(layoutInflater)
        setContentView(ui.root)
        super.onCreate(savedInstanceState)


//        // Créer pour le test un objet sender et receiver
//        sender = Utilisateur(
//            idUtilisateur = "J2jvzJuupSgRyupfk4cEzFOjh8e2",
//            email = "sylvaingossou@gmail.com",
//            nomComplet = "Sylvain GOSSOU",
//            motDePasse = "sylvain",
//            typeUtilisateur = "client"
//        )
//        receiver = Utilisateur(
//            idUtilisateur = "kXllXFn3lLaHzGX2njswBogitiP2",
//            email = "sylvingossou@gmail.com",
//            nomComplet = "Sylvain GG",
//            motDePasse = "sylvain",
//            typeUtilisateur = "conducteur"
//        )


        val adapter = ChatAdapter()
        adapter.onItemClick = this::onItemClick

        lifecycleScope.launch {
            Log.d("Carpooling", "ChatActivity:onCreate ---> WAITING FOR RESULTS")
            if (UtilisateurService.utilisateurActuel == null) {
                Log.d("Carpooling", "ChatActivity:onCreate ---> MANUAL INITIALISATION OF UTILISATEUR")
                UtilisateurService.initialiserUtilisateurActuel("J2jvzJuupSgRyupfk4cEzFOjh8e2")
                Log.d("Carpooling", "ChatActivity:onCreate ---> RESULTS ---> ${UtilisateurService.utilisateurActuel!!.nomComplet}")
                Log.d("Carpooling", "ChatActivity:onCreate ---> USER CHAT-LISTS ---> ${UtilisateurService.utilisateurActuel!!.mesChats}")
            }
            val chats = ChatService.getChatsByIds(UtilisateurService.utilisateurActuel!!.mesChats)
            Log.d("Carpooling", "ChatActivity:onCreate ---> CHATS ---> ${chats.size} ---> $chats")
            adapter.setChats(chats)
            ui.chatRecyclerView.adapter = adapter
        }
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
        val chat = Chat(
            idChat = DiscussionService.generateUniqueKey(),
            nomInitialisateur = sender.nomComplet,
            idInitialisateur = sender.idUtilisateur,
            nomMembreSecondaire = receiver.nomComplet,
            idMembreSecondaire = receiver.idUtilisateur,
        )
        ChatService.creerRemoteChat(chat) // Ajouter le chat au remote
        // Ajouter l'id du chat aux deux utilisateurs
        sender.ajouterIdDiscussion(chat.idChat)
        receiver.ajouterIdDiscussion(chat.idChat)

        testChat.add(chat)
        return testChat
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}