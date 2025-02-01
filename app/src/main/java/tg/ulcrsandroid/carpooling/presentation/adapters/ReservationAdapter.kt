package tg.ulcrsandroid.carpooling.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.databinding.ItemReservationBinding
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.presentation.viewholders.ReservationViewHolder

class ReservationAdapter(
    var reservations: List<Reservation>,
    var lifecycleOwner: LifecycleOwner?
) : RecyclerView.Adapter<ReservationViewHolder>() {

    constructor() : this(emptyList(), null)
    lateinit var onItemClick: (Reservation?) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val ui = ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(ui, lifecycleOwner!!)
    }

    override fun getItemCount(): Int {
        return reservations.size
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.reservation = reservations[position]
        holder.onItemClick = onItemClick
    }

    fun setListReservation(reservs: List<Reservation>) {
        reservations = reservs
        notifyDataSetChanged()
    }

}