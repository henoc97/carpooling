package tg.ulcrsandroid.carpooling.presentation.viewholders

import android.util.Log
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tg.ulcrsandroid.carpooling.application.services.NotificationService
import tg.ulcrsandroid.carpooling.application.services.PassagerService
import tg.ulcrsandroid.carpooling.application.services.ReservationService
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.application.utils.managers.PassagerManager
import tg.ulcrsandroid.carpooling.application.utils.managers.TimeManager
import tg.ulcrsandroid.carpooling.databinding.ItemRechercheTrajetBinding
import tg.ulcrsandroid.carpooling.domain.models.Passager
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
//                reservation.idPassager = PassagerManager.getPassagerActuel()!!.idUtilisateur
                reservation.idTrajet = value.idTrajet
                reservation.heureReservation = System.currentTimeMillis()
                reservation.statut = ReservationService.EN_ATTENTE
                ReservationService.persisterReservation(reservation)
                ui.bouttonReservation.text = "Reservé ✓"
                Log.d("Carpooling", "RechercheTrajetViewHolder:Setter ---> Fin de la reservation ajout de l'objet au trajet")
                // Notif push
                val notificationTitle = "Nouvelle demande de covoiturage"
                val notificationBody = "Vous avez une nouvelle demande de covoiturage."

                // Récupérer le token FCM du conducteur
                UtilisateurService.getFcmTokenById(
                    value.idConducteur,
                    onSuccess = { token ->
                        if (token != null) {
                            // Si le token est récupéré avec succès, envoyer la notification
                            NotificationService.envoyerNotification(token, notificationTitle, notificationBody)
                        } else {
                            Log.e("NotificationService", "Le token FCM du conducteur est null.")
                        }
                    },
                    onError = { errorMessage ->
                        Log.e("NotificationService", errorMessage)
                    }
                )
            }
        }
}