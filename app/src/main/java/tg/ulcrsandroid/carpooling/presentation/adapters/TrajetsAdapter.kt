package tg.ulcrsandroid.carpooling.presentation.adapters

import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrajetsAdapter(
    private val trajets: List<Trajet>,
    private val ontrajetClick: (Trajet) -> Unit
) : RecyclerView.Adapter<TrajetsAdapter.trajetViewHolder>() {

    class trajetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val locationName: TextView = view.findViewById(R.id.locationName)
        val carInfo: TextView = view.findViewById(R.id.carInfo)
        val trajetTime: TextView = view.findViewById(R.id.tripTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): trajetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return trajetViewHolder(view)
    }

    override fun onBindViewHolder(holder: trajetViewHolder, position: Int) {
        val trajet = trajets[position]
        holder.locationName.text = trajet.lieuArrivee
        holder.carInfo.text = trajet.conducteur.detailsVehicule
        holder.trajetTime.text = trajet.heureDepart.toString()
        holder.itemView.setOnClickListener { ontrajetClick(trajet) }
    }

    override fun getItemCount() = trajets.size
}