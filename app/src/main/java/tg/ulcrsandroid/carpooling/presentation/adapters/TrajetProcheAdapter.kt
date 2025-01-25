package tg.ulcrsandroid.carpooling.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import java.text.SimpleDateFormat
import java.util.*

class TrajetProcheAdapter(private val trajets: List<Trajet>) : RecyclerView.Adapter<TrajetProcheAdapter.TrajetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrajetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trajet, parent, false)
        return TrajetViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrajetViewHolder, position: Int) {
        val trajet = trajets[position]
        holder.bind(trajet)
    }

    override fun getItemCount(): Int {
        return trajets.size
    }

    class TrajetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lieuDepart: TextView = itemView.findViewById(R.id.lieuDepart)
        private val lieuArrivee: TextView = itemView.findViewById(R.id.lieuArrivee)
        private val heureDepart: TextView = itemView.findViewById(R.id.heureDepart)
        private val prixParPassager: TextView = itemView.findViewById(R.id.prixParPassager)
        private val placesDisponibles: TextView = itemView.findViewById(R.id.placesDisponibles)

        fun bind(trajet: Trajet) {
            lieuDepart.text = "Départ : ${trajet.lieuDepart}"
            lieuArrivee.text = "Arrivée : ${trajet.lieuArrivee}"
            heureDepart.text = "Heure : ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(trajet.heureDepart)}"
            prixParPassager.text = "Prix : ${trajet.prixParPassager} XOF"
            placesDisponibles.text = "Places : ${trajet.placesDisponibles}"
        }
    }
}