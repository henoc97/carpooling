package tg.ulcrsandroid.carpooling

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur

class CarpoolingApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        UtilisateurService.initialiserIdUtilisateur(this)

        // Adding a custom id for test purpose
        if (UtilisateurService.utilisateurID == null) {
            UtilisateurService.utilisateurID = "aocwcbskjcqwoiqc"
        }

        Log.i("Carpooling", "APPLICATION ---> INITIALISATION DE L'APPLICATION")
        Log.i("Carpooling", "APPLICATION ---> INITIALISATION DE L'ID DE L'UTILISATEUR ---> ${UtilisateurService.utilisateurID}")

    }
}