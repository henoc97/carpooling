package tg.ulcrsandroid.carpooling.presentation.viewholders

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.application.services.ReservationService
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.application.utils.managers.TimeManager
import tg.ulcrsandroid.carpooling.databinding.ItemRechercheTrajetBinding
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.domain.models.Trajet

class RechercheTrajetViewHolder(val ui: ItemRechercheTrajetBinding) : RecyclerView.ViewHolder(ui.root) {

    lateinit var afficherToast: () -> Unit

    var trajet: Trajet?
        get() = null
        set(value) {
            ui.locationName.text = value?.lieuDepart
            ui.tripTime.text = TimeManager.convertTimestampToFormattedDate(value?.heureDepart!!)
            ui.bouttonReservation.setOnClickListener {
                Log.d("Carpooling", "RechercheTrajetViewHolder:Setter ---> Click sur le boutton reserver")
                // Créer une reservation
                val reservation = Reservation()
                reservation.idPassager = UserManager.getCurrentUser()!!.idUtilisateur
                reservation.idTrajet = value.idTrajet
                reservation.heureReservation = System.currentTimeMillis()
                reservation.statut = ReservationService.EN_ATTENTE
                ReservationService.persisterReservation(reservation)
                ui.bouttonReservation.text = "Reservé ✓"
                Log.d("Carpooling", "RechercheTrajetViewHolder:Setter ---> Fin de la reservation ajout de l'objet au trajet")
            }
        }
}