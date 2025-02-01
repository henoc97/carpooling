package tg.ulcrsandroid.carpooling.presentation.adapters

import android.util.Log
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.application.utils.managers.TimeManager

class TrajetsAdapter(
    private var trajets: List<Trajet>,
    private val ontrajetClick: (Trajet) -> Unit
) : RecyclerView.Adapter<TrajetsAdapter.trajetViewHolder>() {

    class trajetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val locationName: TextView = view.findViewById(R.id.locationName)
        val carInfo: TextView = view.findViewById(R.id.carInfo)
        val trajetTime: TextView = view.findViewById(R.id.tripTime)
//        val bouttonReservation: Button = view.findViewById(R.id.bouttonReservation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): trajetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return trajetViewHolder(view)
    }

    override fun onBindViewHolder(holder: trajetViewHolder, position: Int) {
        val trajet = trajets[position]
        holder.locationName.text = "De ${trajet.lieuDepart} à ${trajet.lieuArrivee}"
        holder.carInfo.text = trajet.conducteur?.detailsVehicule
        holder.trajetTime.text = TimeManager.convertTimestampToFormattedDate(trajet.heureDepart)
//        holder.bouttonReservation.setOnClickListener {
//            Log.d("Carpooling", "TrajetsAdapter:onBindViewHolder ---> Click sur le Boutton Reservation")
//        }
        holder.itemView.setOnClickListener { ontrajetClick(trajet) }
    }

    override fun getItemCount() = trajets.size

    fun updateData(newTrajets: List<Trajet>) {
        this.trajets = newTrajets
        notifyDataSetChanged() // Notifier l'adaptateur que les données ont changé
    }
}