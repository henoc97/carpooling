package tg.ulcrsandroid.carpooling.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.TrajetService
import tg.ulcrsandroid.carpooling.application.utils.location.LocationUtils
import tg.ulcrsandroid.carpooling.application.utils.location.getLocationFromCityName
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.presentation.adapters.TrajetProcheAdapter

class TrajetsProchesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var trajetAdapter: TrajetProcheAdapter
    private val trajetsProches = mutableListOf<Trajet>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trajets_proches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser le RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewTrajets)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        trajetAdapter = TrajetProcheAdapter(trajetsProches)
        recyclerView.adapter = trajetAdapter

        // Charger les trajets futurs et les filtrer
        chargerEtFiltrerTrajets()
    }

    private fun chargerEtFiltrerTrajets() {
        // Récupérer la localisation actuelle de l'utilisateur (ici, Lomé)
        val localisationUtilisateur = getLocationFromCityName(requireContext(), "Lomé")
        if (localisationUtilisateur == null) {
            Log.e("TrajetsProchesFragment", "Impossible de récupérer la localisation de l'utilisateur.")
            return
        }

        // Lister les trajets futurs
        TrajetService.listerTrajetsFuturs(
            onSuccess = { trajetsFuturs ->
                // Filtrer les trajets proches de l'utilisateur
                val trajetsFiltres = trajetsFuturs.filter { trajet ->
                    val lieuDepart = getLocationFromCityName(requireContext(), trajet.lieuDepart)
                    if (lieuDepart != null) {
                        // Calculer la distance entre le lieu de départ et la localisation de l'utilisateur
                        val distance = LocationUtils.distanceBetween(localisationUtilisateur, lieuDepart)
                        distance <= 5000 // 5 km en mètres
                    } else {
                        false
                    }
                }

                // Mettre à jour la liste des trajets proches
                trajetsProches.clear()
                trajetsProches.addAll(trajetsFiltres)
                trajetAdapter.notifyDataSetChanged()

                // Ajouter des logs pour déboguer
                Log.d("TrajetsProchesFragment", "Nombre de trajets futurs : ${trajetsFuturs.size}")
                Log.d("TrajetsProchesFragment", "Nombre de trajets filtrés : ${trajetsFiltres.size}")
            },
            onError = { errorMessage ->
                Log.e("TrajetsProchesFragment", errorMessage)
            }
        )
    }
}