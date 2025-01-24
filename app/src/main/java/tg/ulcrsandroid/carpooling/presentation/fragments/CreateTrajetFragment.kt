package tg.ulcrsandroid.carpooling.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.textfield.TextInputEditText
import tg.ulcrsandroid.carpooling.R

class CreateTrajetFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var destinationInput: TextInputEditText
    private lateinit var departureInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
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
        seatsInput = view.findViewById(R.id.seatsInput)
        submitButton = view.findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            createTrip()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Configuration de la carte
    }

    private fun createTrip() {
        val destination = destinationInput.text.toString()
        val departure = departureInput.text.toString()
        val time = timeInput.text.toString()
        val seats = seatsInput.text.toString().toIntOrNull() ?: 0

        // Création du trajet
        // TODO: Implémenter la logique de création
    }
}