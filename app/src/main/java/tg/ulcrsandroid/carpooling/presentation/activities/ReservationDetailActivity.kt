package tg.ulcrsandroid.carpooling.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tg.ulcrsandroid.carpooling.DriveMeToActivity
import tg.ulcrsandroid.carpooling.application.services.ChatService
import tg.ulcrsandroid.carpooling.application.services.PassagerService
import tg.ulcrsandroid.carpooling.application.services.ReservationService
import tg.ulcrsandroid.carpooling.application.services.TrajetService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.application.utils.managers.PassagerManager
import tg.ulcrsandroid.carpooling.databinding.ActivityReservationDetailBinding
import tg.ulcrsandroid.carpooling.domain.models.Reservation

class ReservationDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityReservationDetailBinding
    private var reservation: Reservation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reservation = intent.getParcelableExtra("reservation")
        binding.etat.text = reservation?.statut
        if (reservation?.statut == ReservationService.EN_ATTENTE) {
            binding.etat.setBackgroundColor(Color.hsl(44f, 0.87f, 0.50f).toArgb())
        }

        if (reservation?.statut == ReservationService.REJETEE) {
            binding.etat.setBackgroundColor(Color.hsl(6f, 0.87f, 0.50f).toArgb())
        }

        binding.etat.setBackgroundColor(0)

        binding.envoyerMessage.setOnClickListener(this::envoyerMessage)

        binding.annuler.setOnClickListener(this::annulerReservation)

        binding.rejoindreConducteur.setOnClickListener(this::rejoindreConducteur)

        binding.detailsVehicule.setOnClickListener(this::voirDetailsVehicule)
    }

    private fun voirDetailsVehicule(view: View?) {
        Log.d("Carpooling", "ReservationDetailActivity:voirDetailsVehicule ---> Click sur le boutton details vehicule")
    }

    private fun rejoindreConducteur(view: View?) {
        Log.d("Caarpooling", "ReservationDetailActivity:Reservation===>${reservation!!.trajet}")
        val intent = Intent(this, DriveMeToActivity::class.java)
        intent.putExtra("DESTINATION", reservation!!.trajet!!.lieuDepart)
        startActivity(intent)
    }

    private fun annulerReservation(view: View?) {
        lifecycleScope.launch {
            val bool = TrajetService.supprimerReservation(reservation!!.idTrajet, reservation!!.idReservation)
            if (bool!!) {
                ReservationService.supprimerReservation(reservation!!.idReservation)
                // Supprimer les reservations dans le passager de l'utilisateur actuel
                PassagerService.supprimerReservation(PassagerManager.getPassagerActuel()!!, reservation!!.idReservation)
                Log.d("Carpooling", "ReservationDetailActivity:annulerReservation ---> Reservation correctement supprimer")
            }
            Log.d("Carpooling", "ReservationDetailActivity:annulerReservation ---> Click sur le boutton annuler")
            finish()
        }

    }

    private fun envoyerMessage(view: View?) {
        Log.d("Carpooling", "ReservationDetailActivity:envoyerMessage ---> Click sur le boutton envoyer message")
        // Vérifier si un chat existe entre les deux users
        // Si le chat existe renvoyer l'id du chat à DiscussionActivity
        // Si le chat n'existe pas créer un chat le persister et renvoyer son id à DiscussionActivity
        lifecycleScope.launch {
            val chat = ChatService.findCommonChat(UserManager.getCurrentUser()!!.mesChats, reservation?.trajet!!.idConducteur)
            val intent = Intent(this@ReservationDetailActivity, DiscussionActivity::class.java)
            Log.d("Carpooling", "ReservationDetailActivity:envoyerMessage ---> CHAT ---> $chat")
            intent.putExtra("idChat", chat.idChat)
            intent.putExtra("nomComplet", chat.nomMembreSecondaire)
            startActivity(intent)
        }
    }
}