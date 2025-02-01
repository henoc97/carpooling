package tg.ulcrsandroid.carpooling.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import tg.ulcrsandroid.carpooling.application.services.ChatService
import tg.ulcrsandroid.carpooling.application.services.ReservationService
import tg.ulcrsandroid.carpooling.application.services.TrajetService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.application.utils.managers.TimeManager
import tg.ulcrsandroid.carpooling.databinding.FragmentTripDetailBinding
import tg.ulcrsandroid.carpooling.domain.models.Passager
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.presentation.activities.DiscussionActivity
import tg.ulcrsandroid.carpooling.presentation.adapters.PassengersAdapter

class TrajetDetailFragment : Fragment() {

    private var _binding: FragmentTripDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PassengersAdapter
    private var trajet: Trajet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {        // Initialisation du binding
        _binding = FragmentTripDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Carpooling", "TrajetDetailFragment:onViewCreated ---> Debut de TrajetDetailFragment pour lister les details d'un trajet")

        // Récupération des arguments et affichage des données
        arguments?.let { args ->
            trajet = args.getParcelable<Trajet>("trajet")
            trajet?.let {
                binding.tripDestination.text = "${it.lieuDepart} ⇒ ${it.lieuArrivee}"
                binding.tripTime.text = "Départ : ${TimeManager.convertTimestampToFormattedDate(it.heureDepart)}"
                binding.availableSeats.text = "${it.placesDisponibles} places disponibles"
            }
        }

        lifecycleScope.launch {
            trajet = TrajetService.ajouterObjetsReservations(trajet!!)
            for (i in 0 until trajet!!.reservations!!.size) {
                trajet!!.reservations!![i] = ReservationService.ajouterObjetPassager(trajet!!.reservations!![i])
            }
//            trajet!!.reservations.forEach { reservation ->
//                reservation = ReservationService.ajouterObjetPassager(reservation)
//            }
            // Configuration du RecyclerView avec le binding
            binding.passengersRecyclerView.layoutManager = LinearLayoutManager(context)
            adapter = PassengersAdapter(trajet!!.reservations!!)
            binding.passengersRecyclerView.adapter = adapter
            adapter.supprimerReservationParent = this@TrajetDetailFragment::supprimerReservation
            adapter.envoyerMessageParent = this@TrajetDetailFragment::envoyerMessage
        }
    }

    private fun envoyerMessage(reservation: Reservation) {
        lifecycleScope.launch {
            val chat = ChatService.findCommonChat(UserManager.getCurrentUser()!!.mesChats, reservation?.trajet!!.idConducteur)
            val intent = Intent(requireContext(), DiscussionActivity::class.java)
            Log.d("Carpooling", "ReservationDetailActivity:envoyerMessage ---> CHAT ---> $chat")
            intent.putExtra("idChat", chat.idChat)
            intent.putExtra("nomComplet", chat.nomMembreSecondaire)
            startActivity(intent)
        }
    }

    private fun supprimerReservation(s: String?) {
        Log.d("Carpooling", "TrajetDetailFragment:supprimerReservation ---> s-> $s trajets -> ${trajet?.reservationsIds}")
        trajet?.reservationsIds?.removeAll(listOf(s))
        TrajetService.mettreAJourTrajet(trajet!!)
        Log.d("Carpooling", "TrajetDetailFragment:supprimerReservation ---> trajets -> ${trajet?.reservationsIds}")
    }

    private fun createDummyPassengers(): List<Passager> {
        return listOf(
            Passager(
                idUtilisateur = "1",
                email = "john.doe@example.com",
                nomComplet = "John Doe",
                motDePasse = "password123",
                typeUtilisateur = "passager",
                historiqueReservations = emptyList(), // Vous pouvez ajouter des réservations ici si nécessaire
            ),
            Passager(
                idUtilisateur = "2",
                email = "jane.smith@example.com",
                nomComplet = "Jane Smith",
                motDePasse = "password456",
                typeUtilisateur = "passager",
                historiqueReservations = emptyList(), // Vous pouvez ajouter des réservations ici si nécessaire
            )
        )
    }

    companion object {
        fun newInstance(trajet: Trajet): TrajetDetailFragment {
            return TrajetDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("trajet", trajet)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Libérer le binding pour éviter les fuites de mémoire
        _binding = null
    }
}
