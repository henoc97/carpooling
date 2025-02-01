package tg.ulcrsandroid.carpooling.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import tg.ulcrsandroid.carpooling.databinding.FragmentTripDetailBinding
import tg.ulcrsandroid.carpooling.domain.models.Passager
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.presentation.adapters.PassengersAdapter

class TrajetDetailFragment : Fragment() {

    private var _binding: FragmentTripDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PassengersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialisation du binding
        _binding = FragmentTripDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuration du RecyclerView avec le binding
        binding.passengersRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PassengersAdapter(createDummyPassengers())
        binding.passengersRecyclerView.adapter = adapter

        // Récupération des arguments et affichage des données
        arguments?.let { args ->
            val trajet = args.getParcelable<Trajet>("trajet")
            trajet?.let {
                binding.destinationTitle.text = it.lieuArrivee
                binding.tripTime.text = it.heureDepart.toString()
                binding.availableSeats.text = "${it.placesDisponibles} places disponibles"
            }
        }
    }

    private fun createDummyPassengers(): List<Passager> {
        return listOf(
            Passager(
                idUtilisateur = "1",
                email = "john.doe@example.com",
                nomComplet = "John Doe",
                motDePasse = "password123",
                typeUtilisateur = "passager",
                historiqueReservations = emptyList() // Vous pouvez ajouter des réservations ici si nécessaire
            ),
            Passager(
                idUtilisateur = "2",
                email = "jane.smith@example.com",
                nomComplet = "Jane Smith",
                motDePasse = "password456",
                typeUtilisateur = "passager",
                historiqueReservations = emptyList() // Vous pouvez ajouter des réservations ici si nécessaire
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
