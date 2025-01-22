package tg.ulcrsandroid.carpooling.presentation.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.databinding.ItemChatBinding
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur

class ChatViewHolder(var ui: ItemChatBinding) : RecyclerView.ViewHolder(ui.root) {

    lateinit var onItemClick: (String?, String?) -> Unit
    var idUtilisateur: String? = null
    var nomComplet: String? = null

    var chat: Utilisateur?
        get() = null
        set(value) {
            ui.personName.text = value?.nomComplet
            ui.lastMessage.text = "A mettre à jour"
            idUtilisateur = value?.idUtilisateur
            nomComplet = value?.nomComplet
//            ui.profileImage.setImageResource(R.drawable.ic_profile_picture)
        }

    init {
        ui.root.setOnClickListener(this::onClick)
    }

    private fun onClick(view: View?) {
        onItemClick(idUtilisateur, nomComplet)
    }

}