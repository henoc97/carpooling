package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.launch
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.DiscussionService
import tg.ulcrsandroid.carpooling.application.services.NotificationService
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.databinding.ActivityDiscussionBinding
import tg.ulcrsandroid.carpooling.domain.models.Discussion
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.presentation.adapters.DiscussionAdapter
import java.util.Date

class DiscussionActivity : AppCompatActivity() {

    private lateinit var ui: ActivityDiscussionBinding
    private var database = Firebase.database
//    private var lastMessageIndex: Int = 0
    private var messages: MutableList<Discussion>? = null
    private lateinit var discussionAdapter: DiscussionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityDiscussionBinding.inflate(layoutInflater)
        setContentView(ui.root)

        // Récupérer l'extra contenant l'id de l'utilisateur
        val idChat = intent.getStringExtra("idChat")
        val nomRecepteur = intent.getStringExtra("nomComplet")
        ui.nomDestinataire.text = nomRecepteur

        // Création de la référence de BD pointant vers les discussions
        val ref = database.getReference("chats/$idChat/discussion")

        lifecycleScope.launch {
//            messages = DiscussionService.recupererListDeDiscussions(idChat!!)
            messages = DiscussionService.retreiveMessages(ref)
            Log.d("Carpooling", "DiscussionActivity:onCreate ---> MESSAGES LENGTH---> ${messages?.size}")
//            lastMessageIndex = messages!!.size
            discussionAdapter = DiscussionAdapter(messages!!)
            // Mise en place du viewholder
            ui.messagesRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@DiscussionActivity).apply {
                    stackFromEnd = true // Messages start from bottom
                }
                adapter = discussionAdapter
            }
            // Setup send button click listener
            ui.sendButton.setOnClickListener {
                envoyerMessage(ref)
            }

            // Mise en place d'un ecouteur sur le noeud de message
//            ref.addValueEventListener(object: ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val discussions = snapshot.getValue<MutableList<Discussion>>()
//                    if (discussions != null) {
//                        discussionAdapter.resetDiscussions(discussions)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.d("Carpooling", "DiscussionActivity:onCreate ---> ERROR ---> ${error.message}")
//                }
//            })

            // Initaliser un ecouteur sur les nouveaux messages
            initNewMessageListner(ref)
        }
    }

    private fun initNewMessageListner(ref: DatabaseReference) {
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val msg = snapshot.getValue<Discussion>()!!
                Log.d("Carpooling", "DiscussionActivity:initNewMessageListner ---> CHILD ADDED ---> ${msg}")
                ajouterMessage(msg)
//                lastMessageIndex++
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("Carpooling", "DiscussionActivity:initNewMessageListner ---> CHILD CHANGED")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("Carpooling", "DiscussionActivity:initNewMessageListner ---> CHILD REMOVED")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("Carpooling", "DiscussionActivity:initNewMessageListner ---> CHILD MOVED")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Carpooling", "DiscussionActivity:initNewMessageListner ---> ERROR ---> ${error.message}")
            }
        })
    }

    private fun ajouterMessage(discussion: Discussion) {
        if (discussion.idExpediteur != UserManager.getCurrentUser()?.idUtilisateur) {
//            messages?.add(discussion)
            discussionAdapter.addDiscussion(discussion)
        }
    }

    private fun envoyerMessage(ref : DatabaseReference) {
        val messageText = ui.messageInput.text.toString().trim()
        if (messageText.isNotEmpty()) {
            val discussion = Discussion(
                DiscussionService.generateUniqueKey(),
                UserManager.getCurrentUser()?.idUtilisateur!!,
                messageText,
                Date()
            )
            discussionAdapter.addDiscussion(discussion)
            ref.setValue(discussionAdapter.getDiscussions())
            ui.messageInput.setText("")
        }
    }

}