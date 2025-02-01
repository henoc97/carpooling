package tg.ulcrsandroid.carpooling.presentation.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.textfield.TextInputEditText
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.TrajetService
import tg.ulcrsandroid.carpooling.application.utils.location.*
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.OpenRouteService.fetchRouteFromOpenRouteService
import java.util.*

class SearchTrajetFragment : Fragment(), OnMapReadyCallback {

    // Views
    private lateinit var map: GoogleMap
    private lateinit var destinationInput: TextInputEditText
    private lateinit var departureInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
    private lateinit var submitButton: Button
    private lateinit var distanceSeekBar: SeekBar
    private lateinit var timeSeekBar: SeekBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_trip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        initViews(view)

        // Set up listeners
        setupListeners()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Configure map settings here if needed
    }

    private fun initViews(view: View) {
        // Initialize map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        // Initialize input fields and buttons
        destinationInput = view.findViewById(R.id.destinationInput)
        departureInput = view.findViewById(R.id.departureInput)
        timeInput = view.findViewById(R.id.timeInput)
        submitButton = view.findViewById(R.id.submitButton)
        distanceSeekBar = view.findViewById(R.id.distanceSeekBar)
        timeSeekBar = view.findViewById(R.id.timeSeekBar)
    }

    private fun setupListeners() {
        // Time picker
        timeInput.setOnClickListener {
            showTimePicker()
        }

        // Destination input focus change
        destinationInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                handleLocationInput(destinationInput, "Destination")
            }
        }

        // Departure input focus change
        departureInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                handleLocationInput(departureInput, "Départ")
            }
        }

        // Submit button click
        submitButton.setOnClickListener {
            searchTrips()
        }
    }

    private fun handleLocationInput(input: TextInputEditText, label: String) {
        val locationName = input.text.toString()
        if (locationName.isNotEmpty()) {
            val location = getLocationFromCityName(requireContext(), locationName)
            if (location != null) {
                addMarkerToMap(map, location, "$label: $locationName", moveCamera = false)
                adjustCameraForBothMarkersIfNeeded()
            } else {
                Toast.makeText(requireContext(), "$label introuvable.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                timeInput.setText(formattedTime)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun searchTrips() {
        val destination = destinationInput.text.toString()
        val departure = departureInput.text.toString()
        val time = timeInput.text.toString()

        // Validate inputs
        if (destination.isEmpty() || departure.isEmpty() || time.isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show()
            return
        }

        val timeParts = time.split(":")
        if (timeParts.size != 2 || timeParts[0].toIntOrNull() == null || timeParts[1].toIntOrNull() == null) {
            Toast.makeText(requireContext(), "Format de temps invalide.", Toast.LENGTH_SHORT).show()
            return
        }

        // Parse time
        val selectedHour = timeParts[0].toInt()
        val selectedMinute = timeParts[1].toInt()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, selectedHour)
            set(Calendar.MINUTE, selectedMinute)
            set(Calendar.SECOND, 0)
        }
        val heureDepartTimestamp = calendar.timeInMillis

        // Get tolerances
        val distanceToleranceMeters = distanceSeekBar.progress * 1000 // Convert km to meters
        val timeToleranceMillis = timeSeekBar.progress * 60 * 1000 // Convert minutes to milliseconds

        // Search trips
        TrajetService.rechercherTrajets(
            departure,
            destination,
            heureDepartTimestamp,
            distanceToleranceMeters,
            timeToleranceMillis,
            onSuccess = { trajets ->
                Toast.makeText(requireContext(), "${trajets.size} trajets trouvés.", Toast.LENGTH_SHORT).show()
            },
            onError = { error ->
                Toast.makeText(requireContext(), "Erreur: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun adjustCameraForBothMarkersIfNeeded() {
        val departure = departureInput.text.toString()
        val destination = destinationInput.text.toString()

        if (departure.isNotEmpty() && destination.isNotEmpty()) {
            val startLocation = getLocationFromCityName(requireContext(), departure)
            val endLocation = getLocationFromCityName(requireContext(), destination)

            if (startLocation != null && endLocation != null) {
                adjustCameraForBothMarkers(map, startLocation, endLocation)
                try {
                    fetchRouteFromOpenRouteService(requireContext(), map, startLocation, endLocation)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Erreur de réseau : ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearFields() {
        departureInput.text?.clear()
        destinationInput.text?.clear()
        timeInput.text?.clear()
        map.clear()
    }
}