package tg.ulcrsandroid.carpooling.application.utils.location

import com.google.android.gms.maps.model.LatLng

object LocationUtils {

    // Rayon de la Terre en mètres
    private const val EARTH_RADIUS = 6371000

    /**
     * Calcule la distance entre deux points géographiques en utilisant la formule de Haversine.
     *
     * @param lat1 Latitude du premier point.
     * @param lon1 Longitude du premier point.
     * @param lat2 Latitude du deuxième point.
     * @param lon2 Longitude du deuxième point.
     * @return Distance en mètres.
     */
    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return EARTH_RADIUS * c
    }

    /**
     * Calcule la distance entre deux objets Location.
     *
     * @param location1 Premier point.
     * @param location2 Deuxième point.
     * @return Distance en mètres.
     */
    fun distanceBetween(location1: LatLng, location2: LatLng): Double {
        return haversineDistance(
            location1.latitude,
            location1.longitude,
            location2.latitude,
            location2.longitude
        )
    }
}