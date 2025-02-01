package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.presentation.fragments.TrajetDetailFragment
import tg.ulcrsandroid.carpooling.presentation.fragments.TrajetsListFragment
import tg.ulcrsandroid.carpooling.databinding.ActivityTrajetDetailListBinding

class TrajetListeDetailActivity : AppCompatActivity(), TrajetsListFragment.OnTripSelectedListener {

    // Déclaration du binding
    private lateinit var binding: ActivityTrajetDetailListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisation du binding
        binding = ActivityTrajetDetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Charger le fragment de liste au démarrage
            val trajetsListFragment = TrajetsListFragment()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, trajetsListFragment) // Utilisez binding pour accéder à fragmentContainer
                .commit()
        }
    }

    // Méthode appelée lorsque l'utilisateur sélectionne un trajet
    override fun onTripSelected(trajet: Trajet) {
        val detailFragment = TrajetDetailFragment.newInstance(trajet)

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, detailFragment) // Utilisez binding pour accéder à fragmentContainer
            .addToBackStack(null) // Permet de revenir à la liste avec le bouton retour
            .commit()
    }
}
