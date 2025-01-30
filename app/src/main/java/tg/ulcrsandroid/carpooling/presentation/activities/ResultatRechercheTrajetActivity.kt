package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.application.services.TrajetService
import tg.ulcrsandroid.carpooling.databinding.ActivityResultatRechercheTrajetBinding
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.presentation.adapters.RechercheTrajetAdapter

class ResultatRechercheTrajetActivity: AppCompatActivity() {

    private lateinit var ui: ActivityResultatRechercheTrajetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityResultatRechercheTrajetBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val trajets = intent.getParcelableArrayListExtra<Trajet>("trajets")
        val adapter = RechercheTrajetAdapter(trajets!!.toList())
        adapter.afficherToast = this::afficherToast
//        TrajetService.trajets.
        ui.tripsRecyclerView.adapter = adapter

    }

    private fun afficherToast() {
        Toast.makeText(this, "Reservation éffectuer", Toast.LENGTH_SHORT).show()
        // Ajouter une notification

    }
}