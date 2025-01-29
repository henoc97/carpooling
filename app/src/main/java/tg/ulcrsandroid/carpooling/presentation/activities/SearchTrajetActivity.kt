package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.presentation.fragments.CreateTrajetFragment
import tg.ulcrsandroid.carpooling.presentation.fragments.SearchTrajetFragment

class SearchTrajetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_trajet)

        // Charger le fragment dans le conteneur
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchTrajetFragment())
                .commit()
        }
    }
}
