package tg.ulcrsandroid.carpooling.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.databinding.ActivityChatBinding
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.presentation.adapters.ChatAdapter

class ChatActivity : AppCompatActivity() {

    private lateinit var ui: ActivityChatBinding
    private lateinit var utilisateur: Utilisateur

    override fun onCreate(savedInstanceState: Bundle?) {
        ui = ActivityChatBinding.inflate(layoutInflater)
        setContentView(ui.root)
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

//        utilisateur = UtilisateurService.utilisateurActuel!!
//        val utilisateurList = UtilisateurService.getUsersList(utilisateur.contactsEmails)
        val adapter = ChatAdapter(createTestUsers())
        adapter.onItemClick = this::onItemClick
        ui.chatRecyclerView.adapter = adapter
    }

    private fun onItemClick(idUtilisateur: String?, nomComplet: String?) {
        Log.d("Carpooling", "Utilisateur sélectionné : $nomComplet")
        val intent = Intent(this, DiscussionActivity::class.java)
        intent.putExtra("idUtilisateur", idUtilisateur)
        intent.putExtra("nomComplet", nomComplet)
        startActivity(intent)
    }

    fun createTestUsers(): List<Utilisateur> {
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


}