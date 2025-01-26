//package tg.ulcrsandroid.carpooling.application.utils.location
//
//import android.content.pm.PackageManager
//import androidx.core.content.ContextCompat
//
//private val LOCATION_PERMISSION_REQUEST_CODE = 1
//
//private fun checkLocationPermission() {
//    if (ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//            LOCATION_PERMISSION_REQUEST_CODE
//        )
//    } else {
//        enableUserLocation()
//    }
//}
//
//override fun onRequestPermissionsResult(
//    requestCode: Int,
//    permissions: Array<out String>,
//    grantResults: IntArray
//) {
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
//        grantResults.isNotEmpty() &&
//        grantResults[0] == PackageManager.PERMISSION_GRANTED
//    ) {
//        enableUserLocation()
//    }
//}
