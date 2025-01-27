package tg.ulcrsandroid.carpooling.presentation.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.databinding.ItemChatBinding
import tg.ulcrsandroid.carpooling.domain.models.Chat
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur

class ChatViewHolder(var ui: ItemChatBinding) : RecyclerView.ViewHolder(ui.root) {

    lateinit var onItemClick: (String?, String?) -> Unit
    var idChat: String? = null
    var nomComplet: String? = null

    var chat: Chat?
        get() = null
        set(value) {
            if (UtilisateurService.utilisateurID == value?.idInitialisateur) {
                ui.personName.text = value?.nomMembreSecondaire
                nomComplet = value?.nomMembreSecondaire
            } else {
                ui.personName.text = value?.nomInitialisateur
                nomComplet = value?.nomInitialisateur
            }
            ui.lastMessage.text = "A mettre à jour"
            idChat = value?.idChat
//            ui.profileImage.setImageResource(R.drawable.ic_profile_picture)
        }

    init {
        ui.root.setOnClickListener(this::onClick)
    }

    private fun onClick(view: View?) {
        onItemClick(idChat, nomComplet)
    }

}