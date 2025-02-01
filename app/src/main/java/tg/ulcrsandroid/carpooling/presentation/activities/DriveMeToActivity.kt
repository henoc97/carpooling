package tg.ulcrsandroid.carpooling

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import tg.ulcrsandroid.carpooling.application.utils.location.*
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.OpenRouteService.fetchRouteFromOpenRouteService
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.RouteOSRM.fetchRouteFromOSRM

class DriveMeToActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var destination: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drive_me_to)

        // Récupérer les données d'intention
        destination = intent.getStringExtra("DESTINATION") ?: ""

        // Initialiser FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialiser la carte
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Vérifier et demander les permissions de localisation
        checkLocationPermission()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        enableMyLocation()
        startLocationUpdates()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Mettre à jour la localisation toutes les 10 secondes
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    currentLocation = LatLng(location.latitude, location.longitude)
                    updateMapWithCurrentLocation()
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun updateMapWithCurrentLocation() {
        currentLocation?.let { currentLoc ->
            val destinationLocation = getLocationFromCityName(this, destination)

            if (destinationLocation != null) {
                // Ajuster la caméra pour afficher les deux points
                adjustCameraForBothMarkers(googleMap, currentLoc, destinationLocation)

                // Ajouter des marqueurs
                googleMap.clear() // Effacer les anciens marqueurs
                addMarkerToMap(googleMap, currentLoc, "Vous êtes ici", moveCamera = false)
                addMarkerToMap(googleMap, destinationLocation, "Destination: $destination", moveCamera = false)

                // Tracer la route entre la position actuelle et la destination
                Log.d("DriveMeTo", "Trying to fetchRouteFromOpenRouteService")
                fetchRouteFromOSRM(this, googleMap, currentLoc, destinationLocation)
            } else {
                Log.e("DriveMeToActivity", "Impossible de trouver les coordonnées pour $destination")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Log.e("DriveMeToActivity", "Permission de localisation refusée")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}