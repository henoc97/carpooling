package tg.ulcrsandroid.carpooling.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.databinding.ItemChatBinding
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.presentation.viewholders.ChatViewHolder

class ChatAdapter(val utilisateurList: List<Utilisateur?>) : RecyclerView.Adapter<ChatViewHolder>() {

    lateinit var onItemClick: (String?, String?) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val ui = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(ui)
    }

    override fun getItemCount(): Int {
        return utilisateurList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.chat = utilisateurList[position]
        holder.onItemClick = onItemClick
    }

}