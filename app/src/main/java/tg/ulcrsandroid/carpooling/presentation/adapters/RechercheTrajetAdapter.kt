package tg.ulcrsandroid.carpooling.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.databinding.ItemRechercheTrajetBinding
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.presentation.viewholders.RechercheTrajetViewHolder

class RechercheTrajetAdapter(var trajets: List<Trajet>) : RecyclerView.Adapter<RechercheTrajetViewHolder>() {

    lateinit var afficherToast: () -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RechercheTrajetViewHolder {
        val ui = ItemRechercheTrajetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RechercheTrajetViewHolder(ui)
    }

    override fun getItemCount(): Int {
        return trajets.size
    }

    override fun onBindViewHolder(holder: RechercheTrajetViewHolder, position: Int) {
        holder.trajet = trajets[position]
        holder.afficherToast = afficherToast
    }

}