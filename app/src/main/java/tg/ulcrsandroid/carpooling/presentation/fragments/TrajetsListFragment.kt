package tg.ulcrsandroid.carpooling.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.databinding.FragmentTripsListBinding
import tg.ulcrsandroid.carpooling.domain.models.Conducteur
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.presentation.adapters.TrajetsAdapter

class TrajetsListFragment : Fragment() {

    private var _binding: FragmentTripsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TrajetsAdapter
    private var listener: OnTripSelectedListener? = null

    interface OnTripSelectedListener {
        fun onTripSelected(trajet: Trajet)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTripSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnTripSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialiser le binding
        _binding = FragmentTripsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation du RecyclerView avec binding
        binding.tripsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Créer un adaptateur pour afficher les trajets
        adapter = TrajetsAdapter(createDummyTrips()) { trip ->
            // Navigation vers les détails du trajet
            parentFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, TrajetDetailFragment.newInstance(trip))
                .addToBackStack(null)
                .commit()
        }
        binding.tripsRecyclerView.adapter = adapter
    }

    private fun createDummyTrips(): List<Trajet> {
        val dummyConducteur = Conducteur(
            idUtilisateur = "1",
            email = "conducteur@mail.com",
            nomComplet = "John Doe",
            motDePasse = "password123",
            typeUtilisateur = "conducteur",
            detailsVehicule = "Toyota Corolla",
            placesDisponibles = 3
        )
        return listOf(
            Trajet(
                idTrajet = "1",
                lieuDepart = "Lomé",
                lieuArrivee = "Kara",
                heureDepart = java.util.Date(),
                prixParPassager = 5000f,
                conducteur = dummyConducteur, // Remplace par un objet Conducteur
                placesDisponibles = 3
            ),
            Trajet(
                idTrajet = "2",
                lieuDepart = "Adidogomé",
                lieuArrivee = "Akodésséwa",
                heureDepart = java.util.Date(),
                prixParPassager = 3000f,
                conducteur = dummyConducteur, // Remplace par un objet Conducteur
                placesDisponibles = 2
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Libérer le binding pour éviter les fuites de mémoire
        _binding = null
    }
}
