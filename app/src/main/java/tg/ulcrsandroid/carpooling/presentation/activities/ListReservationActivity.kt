package tg.ulcrsandroid.carpooling.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tg.ulcrsandroid.carpooling.application.services.PassagerService
import tg.ulcrsandroid.carpooling.application.services.ReservationService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.databinding.ActivityListReservationBinding
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.presentation.adapters.ReservationAdapter

class ListReservationActivity: AppCompatActivity() {

    private lateinit var binding: ActivityListReservationBinding
    private lateinit var adapter: ReservationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ReservationAdapter()
        adapter.onItemClick = this::onItemClick

        lifecycleScope.launch {
            PassagerService.recupererPassager(UserManager.getCurrentUser()!!.idUtilisateur)
            adapter.reservations = ReservationService.mesReservations()
            Log.d("Carpooling", "ListReservationActivity:onCreate ---> RESERVATIONS ---> ${adapter.reservations}")
            adapter.lifecycleOwner = this@ListReservationActivity
            binding.tripsRecyclerView.adapter = adapter
        }
    }

    private fun onItemClick(reservation: Reservation?) {
        Log.d("Carpooling", "ListReservationActivity:onItemClick ---> RESERVATION ---> $reservation")
        val intent = Intent(this, ReservationDetailActivity::class.java).apply {
            putExtra("reservation", reservation)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            PassagerService.recupererPassager(UserManager.getCurrentUser()!!.idUtilisateur)
            adapter.setListReservation(ReservationService.mesReservations())
//            adapter.reservations = ReservationService.mesReservations()
            Log.d("Carpooling", "ListReservationActivity:onResume ---> RESERVATIONS ---> ${adapter.reservations}")
        }
    }
}