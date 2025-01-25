package tg.ulcrsandroid.carpooling.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import tg.ulcrsandroid.carpooling.databinding.ItemChatBinding
import tg.ulcrsandroid.carpooling.domain.models.Chat
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.presentation.viewholders.ChatViewHolder

class ChatAdapter(var chatList: List<Chat?>) : RecyclerView.Adapter<ChatViewHolder>() {
    constructor() : this(mutableListOf<Chat>())

    lateinit var onItemClick: (String?, String?) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val ui = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(ui)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.chat = chatList[position]
        holder.onItemClick = onItemClick
    }

    fun setChats(l: MutableList<Chat?>) {
        chatList = l
        Log.d("Carpooling", "ChatAdpater:setChats ---> NOTIFYING THE RECYCLER THAT THE DATA CHANGED !")
//        notifyAll()
        notifyDataSetChanged()
    }

}