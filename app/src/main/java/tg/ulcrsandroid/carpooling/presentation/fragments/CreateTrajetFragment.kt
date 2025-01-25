package tg.ulcrsandroid.carpooling.presentation.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.textfield.TextInputEditText
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.TrajetService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.application.utils.location.*
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.OpenRouteService.fetchRouteFromOpenRouteService
import java.text.SimpleDateFormat
import java.util.*

class CreateTrajetFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var destinationInput: TextInputEditText
    private lateinit var departureInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
    private lateinit var priceInput: TextInputEditText
    private lateinit var seatsInput: TextInputEditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_trip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation de la carte
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialisation des vues
        destinationInput = view.findViewById(R.id.destinationInput)
        departureInput = view.findViewById(R.id.departureInput)
        timeInput = view.findViewById(R.id.timeInput)
        priceInput = view.findViewById(R.id.priceInput)
        seatsInput = view.findViewById(R.id.seatsInput)
        submitButton = view.findViewById(R.id.submitButton)

        // Configurer le TimePicker pour le champ de l'heure
        timeInput.setOnClickListener {
            showTimePicker()
        }

        // Réagir aux changements de texte pour pointer les villes
        destinationInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val destination = destinationInput.text.toString()
                if (destination.isNotEmpty()) {
                    val location = getLocationFromCityName(requireContext(), destination)
                    if (location != null) {
                        addMarkerToMap(map, location, "Destination: $destination", moveCamera = false)
                        adjustCameraForBothMarkersIfNeeded()
                    } else {
                        Toast.makeText(requireContext(), "Ville de destination introuvable.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        departureInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val departure = departureInput.text.toString()
                if (departure.isNotEmpty()) {
                    val location = getLocationFromCityName(requireContext(), departure)
                    if (location != null) {
                        addMarkerToMap(map, location, "Départ: $departure", moveCamera = false)
                        adjustCameraForBothMarkersIfNeeded()
                    } else {
                        Toast.makeText(requireContext(), "Ville de départ introuvable.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        submitButton.setOnClickListener {
            createTrip()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Configuration de la carte
    }

    private fun showTimePicker() {
        // Obtenir l'heure actuelle
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Créer un TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                // Formater l'heure sélectionnée
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                timeInput.setText(formattedTime) // Afficher l'heure dans le champ
            },
            hour,
            minute,
            true // Format 24 heures
        )

        // Afficher le TimePickerDialog
        timePickerDialog.show()
    }

    private fun createTrip() {
        val destination = destinationInput.text.toString()
        val departure = departureInput.text.toString()
        val time = timeInput.text.toString()
        val price = priceInput.text.toString().toFloatOrNull() ?: 0f
        val seats = seatsInput.text.toString().toIntOrNull() ?: 0

        // Vérification des données
        if (destination.isEmpty() || departure.isEmpty() || time.isEmpty() || price <= 0 || seats <= 0) {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs correctement.", Toast.LENGTH_SHORT).show()
            return
        }

        // Convertir la chaîne de temps en Date
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val heureDepart: Date = try {
            dateFormat.parse(time) ?: Date()
        } catch (e: Exception) {
            Date() // En cas d'erreur, utilisez la date actuelle
        }

        // Créer un objet Trajet
        val trajet = Trajet(
            idTrajet = "0",
            lieuDepart = departure,
            lieuArrivee = destination,
            heureDepart = heureDepart,
            prixParPassager = price,
            placesDisponibles = seats,
            conducteur = null // À initialiser avec l'utilisateur connecté
        )

        val idConducteur = UserManager.getCurrentUser()?.idUtilisateur
        if (idConducteur != null) {
            // Création du trajet
            TrajetService.creerTrajet(trajet, idConducteur)
            Toast.makeText(requireContext(), "Trajet créé avec succès.", Toast.LENGTH_SHORT).show()
            clearFields()
        } else {
            Toast.makeText(requireContext(), "Utilisateur non connecté.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun adjustCameraForBothMarkersIfNeeded() {
        val departure = departureInput.text.toString()
        val destination = destinationInput.text.toString()

        if (departure.isNotEmpty() && destination.isNotEmpty()) {
            val startLocation = getLocationFromCityName(requireContext(), departure)
            val endLocation = getLocationFromCityName(requireContext(), destination)

            if (startLocation != null && endLocation != null) {
                // Ajuster la caméra pour afficher les deux points
                Log.d("CreateTrajetFragment", "Start Location: $startLocation")
                Log.d("CreateTrajetFragment", "End Location: $endLocation")
                adjustCameraForBothMarkers(map, startLocation, endLocation)

                // Tracer la route entre les deux points
                // fetchRoute(requireContext(), requireActivity(), map, startLocation, endLocation)
                // drawRoute(map, startLocation, endLocation)
                fetchRouteFromOpenRouteService(requireContext(), map, startLocation, endLocation)
            }
        }
    }

    private fun clearFields() {
        departureInput.text?.clear() // Vider le champ de départ
        destinationInput.text?.clear() // Vider le champ de destination
        timeInput.text?.clear() // Vider le champ de l'heure
        priceInput.text?.clear() // Vider le champ du prix
        seatsInput.text?.clear() // Vider le champ du nombre de places
        map.clear() // Effacer les marqueurs et les tracés sur la carte
    }
}