package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.databinding.ActivityDiscussionBinding
import tg.ulcrsandroid.carpooling.domain.models.Discussion
import tg.ulcrsandroid.carpooling.presentation.adapters.DiscussionAdapter
import java.util.Date

class DiscussionActivity : AppCompatActivity() {

    private lateinit var ui: ActivityDiscussionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityDiscussionBinding.inflate(layoutInflater)
        setContentView(ui.root)

        // Récupérer l'extra contenant l'id de l'utilisateur
        val idRecepteur = intent.getStringExtra("idUtilisateur")
        val nomRecepteur = intent.getStringExtra("nomComplet")

        ui.headerTitle.text = nomRecepteur

        val adapter = DiscussionAdapter(initialiserListDeDiscussionTest())
        ui.messagesRecyclerView.adapter = adapter
        ui.messagesRecyclerView.layoutManager = LinearLayoutManager(this)

        ui.sendButton.setOnClickListener(this::envoyerMessage)
    }

    private fun envoyerMessage(view: View?) {
        val message = ui.messageInput.text.toString()
    }

    private fun initialiserListDeDiscussionTest() : List<Discussion> {
        val discussions = mutableListOf<Discussion>()
        for (i in 1..5) {
            discussions.add(Discussion("Discussion $i", if (i % 2 == 0) UtilisateurService.utilisateurID!! else "autreId", "Message $i", Date()))
        }
        discussions.sortByDescending { it.horodatage }
        return discussions
    }
}