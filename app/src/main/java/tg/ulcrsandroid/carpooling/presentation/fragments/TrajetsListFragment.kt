package tg.ulcrsandroid.carpooling.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import tg.ulcrsandroid.carpooling.application.services.TrajetService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.databinding.FragmentTripsListBinding
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

        // Créer un adaptateur avec une liste vide
        adapter = TrajetsAdapter(emptyList()) { trip ->
            // Navigation vers les détails du trajet
            parentFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, TrajetDetailFragment.newInstance(trip))
                .addToBackStack(null)
                .commit()
        }
        binding.tripsRecyclerView.adapter = adapter

        // Appeler la méthode pour récupérer les trajets
        callTrips(
            onSuccess = { trajets ->
                // Mettre à jour l'adaptateur avec les nouvelles données
                adapter.updateData(trajets)
            },
            onError = { errorMessage ->
                Log.e("TrajetsListFragment", errorMessage)
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun callTrips(onSuccess: (List<Trajet>) -> Unit, onError: (String) -> Unit) {
        val idConducteur = UserManager.getCurrentUser()?.idUtilisateur
        if (idConducteur != null) {
            TrajetService.mesTrajetsCrees(idConducteur,
                onSuccess = { trajets ->
                    onSuccess(trajets) // Retourner les trajets via le callback
                },
                onError = { errorMessage ->
                    onError(errorMessage) // Retourner l'erreur via le callback
                }
            )
        } else {
            onError("Utilisateur non connecté.")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Libérer le binding pour éviter les fuites de mémoire
        _binding = null
    }
}