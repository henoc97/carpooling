package tg.ulcrsandroid.carpooling.presentation.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.domain.models.Passager
import tg.ulcrsandroid.carpooling.R


class PassengersAdapter(
    private val passengers: List<Passager>
) : RecyclerView.Adapter<PassengersAdapter.PassengerViewHolder>() {

    class PassengerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val passengerName: TextView = view.findViewById(R.id.passengerName)
        val passengerPosition: TextView = view.findViewById(R.id.passengerPosition)
        val messageButton: ImageButton = view.findViewById(R.id.messageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_passenger, parent, false)
        return PassengerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {
        val passenger = passengers[position]
        holder.passengerName.text = passenger.nomComplet
        holder.passengerPosition.text = "passenger.position"
        holder.messageButton.setOnClickListener {
            // TODO: Implémenter la messagerie
        }
    }

    override fun getItemCount() = passengers.size
}