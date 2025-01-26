package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialisation du BottomNavigationView et du NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(navController)

        // Utiliser un CoroutineScope pour appeler la fonction suspendue
        CoroutineScope(Dispatchers.Main).launch {
            val currentUser = UtilisateurService.getCurrentUser()
            if (currentUser != null) {
                UserManager.setCurrentUser(currentUser)
            } else {
                println("Aucun utilisateur connecté.")
            }
        }
    }
}
