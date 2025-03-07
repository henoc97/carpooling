package tg.ulcrsandroid.carpooling.presentation.fragments

import android.content.Intent
import androidx.fragment.app.Fragment
import tg.ulcrsandroid.carpooling.R

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.data.model.User
import kotlinx.coroutines.launch
import tg.ulcrsandroid.carpooling.application.services.ChatService
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.databinding.FragmentChatBinding
import tg.ulcrsandroid.carpooling.domain.models.Chat
import tg.ulcrsandroid.carpooling.presentation.activities.DiscussionActivity
import tg.ulcrsandroid.carpooling.presentation.adapters.ChatAdapter

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ChatAdapter()
        adapter.onItemClick = this::onItemClick
        binding.chatRecyclerView.adapter = adapter
        lifecycleScope.launch {
            Log.d("Carpooling", "ChatFragment:onViewCreated ---> Mise à jour de l'user")
            UserManager.setCurrentUser(UtilisateurService.getCurrentUser())
            Log.d("Carpooling", "ChatFragment:onViewCreated ---> Chats ---> ${UserManager.getCurrentUser()!!}")
            val chats = ChatService.getChatsByIds(UserManager.getCurrentUser()!!.mesChats)
            Log.d("Carrpooling", "ChatFragment:onViewCreated ---> Chats ---> $chats")
            adapter.setChats(chats)
        }
    }

    private fun onItemClick(idChat: String?, nomComplet: String?) {
        Log.d("Carpooling", "ChatFragment:onItemClick ---> UTILISATEUR SELECTIONNE ---> $nomComplet")
        val intent = Intent(requireContext(), DiscussionActivity::class.java)
        intent.putExtra("idChat", idChat)
        Log.d("Carpooling", "ChatFragment:onItemClick ---> ID-CHAT SHARED ---> $idChat")
        intent.putExtra("nomComplet", nomComplet)
        startActivity(intent)
    }
}