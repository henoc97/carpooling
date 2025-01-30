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
import kotlinx.coroutines.launch
import tg.ulcrsandroid.carpooling.application.services.ChatService
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.databinding.FragmentChatBinding
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
        lifecycleScope.launch {
            val chats = ChatService.getChatsByIds(UserManager.getCurrentUser()!!.mesChats)
            adapter.setChats(chats)
            binding.chatRecyclerView.adapter = adapter
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