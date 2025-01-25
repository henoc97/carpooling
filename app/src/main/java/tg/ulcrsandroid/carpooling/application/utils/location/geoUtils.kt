package tg.ulcrsandroid.carpooling.application.utils.location

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.IOException
import java.util.Locale
import com.google.maps.android.PolyUtil
import okhttp3.*
import org.json.JSONObject

/**
 * Convertit un nom de ville en coordonnées géographiques (LatLng).
 */
fun getLocationFromCityName(context: Context, cityName: String): LatLng? {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addresses = geocoder.getFromLocationName(cityName, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val location = addresses[0]
            LatLng(location.latitude, location.longitude)
        } else {
            null
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}

/**
 * Ajoute un marqueur sur la carte et centre la caméra sur ce marqueur.
 */
fun addMarkerToMap(map: GoogleMap, location: LatLng, title: String, moveCamera: Boolean = true, zoomLevel: Float = 10f) {
    map.addMarker(MarkerOptions().position(location).title(title))
    if (moveCamera) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
    }
}

/**
 * Trace une ligne droite entre deux points sur la carte.
 */
fun drawRoute(map: GoogleMap, start: LatLng, end: LatLng) {
    val polylineOptions = PolylineOptions()
        .add(start)
        .add(end)
        .width(10f) // Épaisseur de la ligne
        .color(Color.BLUE) // Couleur de la ligne

    map.addPolyline(polylineOptions)
}

/**
 * Ajuste la caméra pour afficher deux marqueurs sur la carte.
 */
fun adjustCameraForBothMarkers(map: GoogleMap, start: LatLng, end: LatLng, padding: Int = 100) {
    val bounds = LatLngBounds.Builder()
        .include(start)
        .include(end)
        .build()

    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
    map.moveCamera(cameraUpdate)
}

/**
 * Récupère et trace une route précise entre deux points en utilisant l'API Directions.
 */
fun fetchRoute(context: Context, activity: Activity?, map: GoogleMap, start: LatLng, end: LatLng) {
    val apiKey: String = "AIzaSyCNwArZMo4NfCOfE6ANRi-vNmzFZeEtBaM"
    val url = "https://maps.googleapis.com/maps/api/directions/json?" +
            "origin=${start.latitude},${start.longitude}&" +
            "destination=${end.latitude},${end.longitude}&" +
            "key=$apiKey"

    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // Gérer l'erreur (par exemple, afficher un Toast)
            Log.e("fetchRoute", "Erreur lors de la récupération de la route.")
            Toast.makeText(context, "Erreur lors de la récupération de la route.", Toast.LENGTH_SHORT).show()
        }

        override fun onResponse(call: Call, response: Response) {
            val jsonData = response.body?.string()
            Log.d("fetchRoute", "API Response: $jsonData") // Ajoutez ce log

            if (jsonData != null) {
                val jsonObject = JSONObject(jsonData)
                val routes = jsonObject.getJSONArray("routes")
                if (routes.length() > 0) {
                    val points = routes.getJSONObject(0)
                        .getJSONObject("overview_polyline")
                        .getString("points")
                    val decodedPath = PolyUtil.decode(points)

                    // Tracer la route sur la carte
                    (context as? android.app.Activity)?.runOnUiThread {
                        val polylineOptions = PolylineOptions()
                            .addAll(decodedPath)
                            .width(10f)
                            .color(Color.BLUE)
                        map.addPolyline(polylineOptions)
                    }
                } else {
                    Log.e("fetchRoute", "Aucune route trouvée dans la réponse de l'API.")
                }
            } else {
                Log.e("fetchRoute", "Réponse de l'API vide.")
            }
        }
    })
}