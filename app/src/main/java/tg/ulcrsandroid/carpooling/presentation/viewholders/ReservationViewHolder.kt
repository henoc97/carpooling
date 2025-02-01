package tg.ulcrsandroid.carpooling.presentation.viewholders

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import tg.ulcrsandroid.carpooling.application.services.ReservationService
import tg.ulcrsandroid.carpooling.application.utils.managers.TimeManager
import tg.ulcrsandroid.carpooling.databinding.ItemReservationBinding
import tg.ulcrsandroid.carpooling.domain.models.Reservation

class ReservationViewHolder(
    val ui: ItemReservationBinding,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(ui.root) {

    lateinit var onItemClick: (Reservation?) -> Unit
    private var reserv: Reservation? = null

    init {
        ui.root.setOnClickListener(this::onClick)
    }

    private fun onClick(view: View?) {
        onItemClick(reserv)
    }

    var reservation: Reservation?
        get() = null
        set(value) {
            lifecycleOwner.lifecycleScope.launch {
                reserv = ReservationService.ajouterObjetTrajet(value!!)
                Log.d("Carpooling", "ReservationViewHolder:onClick ---> Reservation ---> $reserv")
                ui.departDestination.text = "${reserv!!.trajet?.lieuDepart} ⇒ ${reserv!!.trajet?.lieuArrivee}"
                ui.carInfo.text = "Toyota corolla cor"
                ui.etat.text = reserv!!.statut
                ui.dateDepart.text = TimeManager.convertTimestampToFormattedDate(reserv!!.trajet?.heureDepart!!)
            }
        }
}