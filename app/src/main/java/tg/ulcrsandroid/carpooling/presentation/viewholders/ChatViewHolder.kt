package tg.ulcrsandroid.carpooling.presentation.viewholders

import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.databinding.ItemChatBinding
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur

class ChatViewHolder(var ui: ItemChatBinding) : RecyclerView.ViewHolder(ui.root) {

    var chat: Utilisateur?
        get() = null
        set(value) {
            ui.personName.text = value?.nomComplet
            ui.lastMessage.text = "A mettre à jour"
//            ui.profileImage.setImageResource(R.drawable.ic_profile_picture)
        }
}