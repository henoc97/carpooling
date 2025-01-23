package tg.ulcrsandroid.carpooling.presentation.viewholders

import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.databinding.ItemDiscussionBinding
import tg.ulcrsandroid.carpooling.domain.models.Discussion
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur

class DiscussionViewHolder(val ui: ItemDiscussionBinding) : RecyclerView.ViewHolder(ui.root) {

    var discussion: Discussion?
        get() = null
        set(value) {
            ui.message.text = value?.message
            if (value?.idExpediteur != UtilisateurService.utilisateurID) {
                Log.d("Carpooling", "DiscussionViewHolder ---> EXPEDITEUR ---> ${value?.message}")
                ui.message.setTextColor(ContextCompat.getColor(ui.root.context, R.color.black))
                ui.root.setCardBackgroundColor(ContextCompat.getColor(ui.root.context, R.color.custom_gray))
            }
//            else {
//                ui.root.textAlignment = Gravity.END
//            }
        }

}