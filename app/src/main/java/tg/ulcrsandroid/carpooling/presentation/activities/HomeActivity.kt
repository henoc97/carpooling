package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import tg.ulcrsandroid.carpooling.R

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialiser la carte
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Configurer la carte
        val defaultLocation = LatLng(48.8566, 2.3522) // Paris, France
        map.addMarker(MarkerOptions().position(defaultLocation).title("Marker in Paris"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
    }
}