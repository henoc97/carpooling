package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.R
import java.util.Date
import tg.ulcrsandroid.carpooling.domain.models.Conducteur
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.presentation.fragments.TrajetDetailFragment

class TrajetDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trajet_detail)

        // Création d'un trajet fictif pour tester
        val dummyConducteur = Conducteur(
            idUtilisateur = "1",
            email = "conducteur@mail.com",
            nomComplet = "John Doe",
            motDePasse = "password123",
            typeUtilisateur = "conducteur",
            detailsVehicule = "Toyota Corolla",
            placesDisponibles = 3
        )

        val dummyTrajet = Trajet(
            idTrajet = "T123",
            lieuDepart = "Lomé",
            lieuArrivee = "Kara",
            heureDepart = System.currentTimeMillis(), // Remplacer par un objet Date correct
            prixParPassager = 5000f,
            conducteur = dummyConducteur,
            placesDisponibles = 3
        )

        // Chargement du fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TrajetDetailFragment.newInstance(dummyTrajet))
                .commit()
        }
    }
}
