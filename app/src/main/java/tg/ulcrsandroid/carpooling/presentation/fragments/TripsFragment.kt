package tg.ulcrsandroid.carpooling.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.databinding.FragmentTripDetailBinding
import tg.ulcrsandroid.carpooling.domain.models.Conducteur
import tg.ulcrsandroid.carpooling.domain.models.Passager
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.presentation.adapters.PassengersAdapter
import tg.ulcrsandroid.carpooling.presentation.adapters.TrajetsAdapter
import java.util.Date

class TripsFragment : Fragment() {

    private var _binding: FragmentTripDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TrajetsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Configuration du RecyclerView avec le binding
//        binding.passengersRecyclerView.layoutManager = LinearLayoutManager(context)
////        adapter = TrajetsAdapter(createDummyReservations())
//        binding.passengersRecyclerView.adapter = adapter
//
//        // Récupération des arguments et affichage des données
//        arguments?.let { args ->
//            val trajet = args.getParcelable<Trajet>("trajet")
//            trajet?.let {
////                binding.destinationTitle.text = it.lieuArrivee
//                binding.tripTime.text = it.heureDepart.toString()
//                binding.availableSeats.text = "${it.placesDisponibles} places disponibles"
//            }
//        }
//    }

//    private fun createDummyReservations(): List<Reservation> {
//        return listOf(
//            Reservation(
//                idReservation = "R001",
//                passager = Passager.createFromParcel(
//                    idUtilisateur = "1",
//                    email = "john.doe@example.com",
//                    nomComplet = "John Doe",
//                    motDePasse = "password123",
//                    typeUtilisateur = "passager",
//                ),
//                trajet = Trajet.createFromParcel(
//                    idTrajet = "T001",
//                    lieuDepart = "Paris",
//                    lieuArrivee = "Lyon",
//                    heureDepart = Date(),
//                    prixParPassager = 10.0f,
//                ),
//                heureReservation = Date(),
//                statut = "pending"
//            )
//
//            Reservation(
//                idReservation = "R002",
//                passager = Passager.createFromParcel(
//                    idUtilisateur = "2",
//                    email = "jane.smith@example.com",
//                    nomComplet = "Jane Smith",
//                    motDePasse = "password456",
//                    typeUtilisateur = "passager",
//                ),
//                trajet = Trajet.createFromParcel(
//                    idTrajet = "T002",
//                    lieuDepart = "Marseille",
//                    lieuArrivee = "Paris",
//                    heureDepart = Date(),
//                    prixParPassager = 15.0f,
//                ),
//                heureReservation = Date(),
//                statut = "accepted"
//            )
//        )
//    }
}