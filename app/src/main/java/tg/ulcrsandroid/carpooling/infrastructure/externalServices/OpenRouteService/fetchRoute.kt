package tg.ulcrsandroid.carpooling.infrastructure.externalServices.OpenRouteService

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import org.json.JSONException

fun fetchRouteFromOpenRouteService(context: Context, map: GoogleMap, start: LatLng, end: LatLng) {
    val  apiKey: String = "5b3ce3597851110001cf62480ff8e51dafe64364bc52cd3e6de97fe9"
    val url = "https://api.openrouteservice.org/v2/directions/driving-car?" +
            "api_key=$apiKey&" +
            "start=${start.longitude},${start.latitude}&" +
            "end=${end.longitude},${end.latitude}"

    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("fetchRoute", "Erreur de réseau: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val jsonData = response.body?.string()
            Log.d("fetchRoute", "Réponse de l'API: $jsonData") // Ajoutez ce log

            if (jsonData != null) {
                try {
                    val jsonObject = JSONObject(jsonData)
                    if (jsonObject.has("features")) {
                        val features = jsonObject.getJSONArray("features")
                        if (features.length() > 0) {
                            val feature = features.getJSONObject(0)
                            if (feature.has("geometry")) {
                                val geometry = feature.getJSONObject("geometry")
                                if (geometry.has("coordinates")) {
                                    val coordinates = geometry.getJSONArray("coordinates")

                                    // Convertir les coordonnées en liste de LatLng
                                    val decodedPath = mutableListOf<LatLng>()
                                    for (i in 0 until coordinates.length()) {
                                        val coord = coordinates.getJSONArray(i)
                                        val latLng = LatLng(coord.getDouble(1), coord.getDouble(0))
                                        decodedPath.add(latLng)
                                    }

                                    // Tracer la route sur la carte
                                    (context as? android.app.Activity)?.runOnUiThread {
                                        val polylineOptions = PolylineOptions()
                                            .addAll(decodedPath)
                                            .width(10f)
                                            .color(Color.BLUE)
                                        map.addPolyline(polylineOptions)
                                    }
                                } else {
                                    Log.e("fetchRoute", "Le champ 'coordinates' est manquant dans la réponse de l'API.")
                                }
                            } else {
                                Log.e("fetchRoute", "Le champ 'geometry' est manquant dans la réponse de l'API.")
                            }
                        } else {
                            Log.e("fetchRoute", "Aucune feature trouvée dans la réponse de l'API.")
                        }
                    } else {
                        Log.e("fetchRoute", "Le champ 'features' est manquant dans la réponse de l'API.")
                    }
                } catch (e: JSONException) {
                    Log.e("fetchRoute", "Erreur lors de l'analyse de la réponse JSON: ${e.message}")
                }
            } else {
                Log.e("fetchRoute", "Réponse de l'API vide.")
            }
        }
    })
}

private fun decodePolyline(encoded: String): List<LatLng> {
    val poly = mutableListOf<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val latLng = LatLng(lat / 1E5, lng / 1E5)
        poly.add(latLng)
    }

    return poly
}