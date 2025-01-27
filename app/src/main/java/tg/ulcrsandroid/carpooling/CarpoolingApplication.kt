package tg.ulcrsandroid.carpooling

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import tg.ulcrsandroid.carpooling.application.services.DiscussionService
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.presentation.activities.ChatActivity
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CarpoolingApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}