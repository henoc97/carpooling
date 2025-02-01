package tg.ulcrsandroid.carpooling.presentation.adapters


import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import tg.ulcrsandroid.carpooling.domain.models.Passager
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.ChatService
import tg.ulcrsandroid.carpooling.application.services.NotificationService
import tg.ulcrsandroid.carpooling.application.services.ReservationService
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.presentation.activities.DiscussionActivity

/**
 * Adapter pour la liste des reservation d'un trajet.
 * Chaque reservation est représentée par un passager de la liste.
 */
class PassengersAdapter(
    private val reservations: MutableList<Reservation>
) : RecyclerView.Adapter<PassengersAdapter.PassengerViewHolder>() {

    lateinit var supprimerReservationParent: (String?) -> Unit
    lateinit var confirmerReservationParent: () -> Unit
    lateinit var envoyerMessageParent: (Reservation) -> Unit
    // lateinit var ajouterObjetTrajet: (Reservation) -> Unit

    class PassengerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val passengerName: TextView = view.findViewById(R.id.passengerName)
        val passengerPosition: TextView = view.findViewById(R.id.passengerPosition)
        val messageButton: ImageButton = view.findViewById(R.id.messageButton)
        val rejeterButton: Button = view.findViewById(R.id.rejeter)
        val confirmerButton: Button = view.findViewById(R.id.confirmer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_passenger, parent, false)
        return PassengerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {
        val passenger = reservations[position].passager
        holder.passengerName.text = passenger?.nomComplet
        // holder.passengerPosition.text = "passenger.position"
        holder.messageButton.setOnClickListener {
            envoyerMessage(reservations[position])
        }
        holder.rejeterButton.setOnClickListener {
            rejeterReservation(reservations[position], position)
            val notificationTitle = "Refus de covoiturage"
            val notificationBody = "Votre demande de covoiturage a été refusée"

            // Récupérer l'activité à partir du contexte
            val context = holder.itemView.context
            if (context is Activity) {
                NotificationService.setActivity(context)
            } else {
                Log.e("NotificationService", "Le contexte n'est pas une activité.")
            }

            // Récupérer le token FCM du passager
            UtilisateurService.getFcmTokenById(
                passenger?.idUtilisateur ?: "", // Utilisez l'ID du passager ici
                onSuccess = { token ->
                    if (token != null) {
                        // Si le token est récupéré avec succès, envoyer la notification
                        NotificationService.envoyerNotification(token, notificationTitle, notificationBody)
                    } else {
                        Log.e("NotificationService", "Le token FCM du passager est null.")
                    }
                },
                onError = { errorMessage ->
                    Log.e("NotificationService", errorMessage)
                }
            )
        }
        if (reservations[position].statut != ReservationService.ACCEPTEE) {
            holder.confirmerButton.setOnClickListener {
                Log.d("Carpooling", "trajet -> ")
                confirmerReservation(reservations[position])
                holder.confirmerButton.text = "Confimé ✓"
                // ajouterObjetTrajet(reservations[position])
                // ReservationService.ajouterObjetTrajet(reservations[position])
                val notificationTitle = "Acceptation de covoiturage"
                Log.d("Carpooling", "trajet -> ${reservations[position]}")
                val notificationBody = "Votre demande de covoiturage  a été acceptée ✓"

                // Récupérer l'activité à partir du contexte
                val context = holder.itemView.context
                if (context is Activity) {
                    NotificationService.setActivity(context)
                } else {
                    Log.e("NotificationService", "Le contexte n'est pas une activité.")
                }

                // Récupérer le token FCM du passager
                UtilisateurService.getFcmTokenById(
                    passenger?.idUtilisateur ?: "", // Utilisez l'ID du passager ici
                    onSuccess = { token ->
                        if (token != null) {
                            // Si le token est récupéré avec succès, envoyer la notification
                            NotificationService.envoyerNotification(token, notificationTitle, notificationBody)
                        } else {
                            Log.e("NotificationService", "Le token FCM du passager est null.")
                        }
                    },
                    onError = { errorMessage ->
                        Log.e("NotificationService", errorMessage)
                    }
                )
            }
        }
    }

    private fun envoyerMessage(reservation: Reservation) {
        Log.d("Carpooling", "PassengersAdapter:onBindViewHolder ---> Click sur le Boutton Message")
        envoyerMessageParent(reservation)
//        lifecycleScope.launch {
//            val chat = ChatService.findCommonChat(UserManager.getCurrentUser()!!.mesChats, reservation?.trajet!!.idConducteur)
//            val intent = Intent(this@ReservationDetailActivity, DiscussionActivity::class.java)
//            Log.d("Carpooling", "ReservationDetailActivity:envoyerMessage ---> CHAT ---> $chat")
//            intent.putExtra("idChat", chat.idChat)
//            intent.putExtra("nomComplet", chat.nomMembreSecondaire)
//            startActivity(intent)
//        }
    }

    private fun confirmerReservation(reservation: Reservation) {
        reservation.statut = ReservationService.ACCEPTEE
        ReservationService.mettreAJourReservation(reservation)
        // Supprimer la reservation de la liste des reservations
        Log.d("Carpooling", "PassengersAdapter:onBindViewHolder ---> Click sur le Boutton Confirmer")
    }

    private fun rejeterReservation(reservation: Reservation, position: Int) {
        reservation.statut = ReservationService.REJETEE
        ReservationService.mettreAJourReservation(reservation)
        supprimerReservationParent(reservation.idReservation) // Suprime la reservation de l'objet trajet
        reservations.removeAll(listOf(reservation))
        notifyItemRemoved(position)
        // Supprimer la reservation de la liste des reservations
        Log.d("Carpooling", "PassengersAdapter:onBindViewHolder ---> Click sur le Boutton Rejeter")
    }

    override fun getItemCount() = reservations.size
}