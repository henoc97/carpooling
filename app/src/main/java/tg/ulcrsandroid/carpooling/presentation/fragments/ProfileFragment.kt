package tg.ulcrsandroid.carpooling.presentation.fragments

import android.content.Intent
import androidx.fragment.app.Fragment
import tg.ulcrsandroid.carpooling.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import tg.ulcrsandroid.carpooling.MainActivity
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.domain.models.Conducteur

class ProfileFragment : Fragment() {
    private lateinit var nomInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var marqueInput: EditText
    private lateinit var matInput: EditText
    private lateinit var btnMettreAJourNom: ImageButton
    private lateinit var btnMettreAJourEmail: ImageButton
    private lateinit var btnMarque: ImageButton
    private lateinit var btnMatricule: ImageButton
    private lateinit var btnDeconnexion: Button
    private var current_user = UserManager.getCurrentUser()
    private val utilisateurService : UtilisateurService = UtilisateurService
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()

        // Récupération des éléments de la vue
        emailInput = view.findViewById(R.id.emailInput)
        nomInput = view.findViewById(R.id.usernameInput)
        marqueInput = view.findViewById(R.id.marqueInput)
//        matInput = view.findViewById(R.id.matInput)
        btnMettreAJourNom = view.findViewById(R.id.modifyUsernameButton)
        btnMettreAJourEmail = view.findViewById(R.id.modifyEmailButton)
        btnMarque = view.findViewById(R.id.btnMarque)
//        btnMatricule = view.findViewById(R.id.btnMatricule)
        btnDeconnexion = view.findViewById(R.id.deconnexion)


        //Recuperation des infos du user
        val ancienEmail = current_user?.email ?: "Email inconnu"
        val ancienNom = current_user?.nomComplet ?: "Nom inconnu"
        var ancienMarque = "Pas de voiture"
        
        // Préremplit les EditText
        nomInput.setText(ancienNom)
        emailInput.setText(ancienEmail)

        fun preremplirChampMarque() {
            if (current_user != null && current_user is Conducteur) {
                //pre-remplir le champ
                ancienMarque = (current_user as? Conducteur)?.detailsVehicule ?: "Pas de voiture"
                marqueInput.setText(ancienMarque)
            }
        }

        //Mise à jour email
        btnMettreAJourEmail.setOnClickListener {
            val newEmail = emailInput.text.toString().trim()

            if (newEmail == ancienEmail) {
                Toast.makeText(requireContext(), "Aucune modification détectée", Toast.LENGTH_SHORT).show()
            } else {

                UtilisateurService.mettreAJourProfil(newEmail, ancienNom)
                Toast.makeText(requireContext(), "Email mis à jour avec succès !", Toast.LENGTH_SHORT).show()
            }
        }

        //Mise à jour nom
        btnMettreAJourNom.setOnClickListener {
            val newNom = nomInput.text.toString().trim()

            if (newNom == ancienNom) {
                Toast.makeText(requireContext(), "Aucune modification détectée", Toast.LENGTH_SHORT).show()
            } else {

                UtilisateurService.mettreAJourProfil(ancienEmail, newNom)
                Toast.makeText(requireContext(), "Email mis à jour avec succès !", Toast.LENGTH_SHORT).show()
            }
        }
        //Deconnexion
        btnDeconnexion.setOnClickListener {
            // Déconnexion de Firebase
            auth.signOut()

            // Nettoyage des données utilisateur (si nécessaire)
            UserManager.clearCurrentUser()

            // Redirection vers l'écran de connexion
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Supprime la pile d'activités
            startActivity(intent)

            // Terminer l'activité actuelle pour éviter de revenir en arrière
            requireActivity().finish()
        }

        //Modifiaction des infos sur la voiture
        btnMarque.setOnClickListener {
            val newMarque = marqueInput.text.toString().trim()
        }

        return view
    }

}
