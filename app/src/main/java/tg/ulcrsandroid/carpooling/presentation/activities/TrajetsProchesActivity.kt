package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.presentation.fragments.TrajetsProchesFragment

class TrajetsProchesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trajets_proches)

        // Charger le fragment TrajetsProchesFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TrajetsProchesFragment())
                .commit()
        }
    }
}