package tg.ulcrsandroid.carpooling.presentation.fragments

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.databinding.FragmentHomeBinding
import tg.ulcrsandroid.carpooling.presentation.activities.CreateTrajetActivity
import tg.ulcrsandroid.carpooling.presentation.activities.SearchTrajetActivity
import tg.ulcrsandroid.carpooling.presentation.activities.TrajetListeDetailActivity

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialiser le View Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser le FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialiser la carte
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Click sur la vue pour choisir sa destination
//        binding.choisirDestSurMap.setOnClickListener {
//            Log.d("Carpooling", "HomeFragment:onViewCreated ---> Click pour choisir sa destination")
//            val intent = Intent(requireActivity(), SearchTrajetActivity::class.java)
//            startActivity(intent)
//        }

        // Ajouter un ClickListener sur le "gerer trajet"
        binding.floatingImage.setOnClickListener {
            Log.d("Carpooling", "HomeFragment:onViewCreated ---> Click pour choisir sa destination")
            // Rediriger vers TrajetListeDetailActivity
            val intent = Intent(requireActivity(), SearchTrajetActivity::class.java)
            startActivity(intent)
        }

        // Ajouter un ClickListener sur le "gerer trajet"
        binding.gererTrajets.setOnClickListener {
            // Rediriger vers TrajetListeDetailActivity
            val intent = Intent(requireActivity(), TrajetListeDetailActivity::class.java)
            startActivity(intent)
        }

        // Ajouter un ClickListener sur le New Trip Card
        binding.newTripCard.setOnClickListener {
            // Rediriger vers CreateTrajetActivity
            val intent = Intent(requireActivity(), CreateTrajetActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        checkLocationPermission() // Vérifie ou demande les permissions nécessaires
        checkGpsEnabled() // Vérifie si le GPS est activé
    }

    // Vérifie si la permission de localisation est accordée
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            enableUserLocation()
        }
    }

    // Active la localisation de l'utilisateur sur la carte
    private fun enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        }
    }

    // Récupère la localisation actuelle de l'utilisateur
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    map.addMarker(MarkerOptions().position(currentLocation).title("Vous êtes ici"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                } else {
                    Toast.makeText(requireContext(), "Impossible de récupérer la localisation", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Vérifie si le GPS est activé
    private fun checkGpsEnabled() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        settingsClient.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                // GPS activé
                getCurrentLocation()
            }
            .addOnFailureListener { exception ->
                // GPS désactivé, demandez à l'utilisateur d'activer le GPS
                if (exception is ResolvableApiException) {
                    try {
                        exception.startResolutionForResult(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE)
                    } catch (ex: IntentSender.SendIntentException) {
                        ex.printStackTrace()
                    }
                }
            }
    }
}