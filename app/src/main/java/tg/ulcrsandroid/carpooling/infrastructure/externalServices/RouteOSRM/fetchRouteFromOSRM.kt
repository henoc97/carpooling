package tg.ulcrsandroid.carpooling.infrastructure.externalServices.RouteOSRM

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import android.graphics.Color
import okhttp3.Response
import org.json.JSONException
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.OpenRouteService.decodePolyline
import java.io.IOException

fun fetchRouteFromOSRM(context: Context, map: GoogleMap, start: LatLng, end: LatLng) {
    val url = "http://router.project-osrm.org/route/v1/driving/${start.longitude},${start.latitude};${end.longitude},${end.latitude}?overview=full"

    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("fetchRoute", "Erreur de réseau: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val jsonData = response.body?.string()
            Log.d("fetchRoute", "Réponse de l'API: $jsonData")

            if (jsonData != null) {
                try {
                    val jsonObject = JSONObject(jsonData)
                    if (jsonObject.has("routes")) {
                        val routes = jsonObject.getJSONArray("routes")
                        if (routes.length() > 0) {
                            val route = routes.getJSONObject(0)
                            val geometry = route.getString("geometry")

                            // Décoder la polyline
                            val decodedPath = decodePolyline(geometry)

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
                        Log.e("fetchRoute", "Le champ 'routes' est manquant dans la réponse de l'API.")
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