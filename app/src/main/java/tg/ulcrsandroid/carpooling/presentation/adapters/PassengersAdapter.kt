package tg.ulcrsandroid.carpooling.presentation.adapters


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
import tg.ulcrsandroid.carpooling.application.services.ReservationService
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
        holder.passengerPosition.text = "passenger.position"
        holder.messageButton.setOnClickListener {
            envoyerMessage(reservations[position])
        }
        holder.rejeterButton.setOnClickListener {
            rejeterReservation(reservations[position], position)
        }
        holder.confirmerButton.setOnClickListener {
            confirmerReservation(reservations[position])
            holder.confirmerButton.text = "Confimé ✓"
            confirmerReservationParent()
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