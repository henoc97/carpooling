package tg.ulcrsandroid.carpooling.presentation.fragments

import androidx.fragment.app.Fragment
import tg.ulcrsandroid.carpooling.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.UserManager

class ProfileFragment : Fragment() {
    private lateinit var nomInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var btnMettreAJourNom: ImageButton
    private lateinit var btnMettreAJourEmail: ImageButton
    private var current_user = UserManager.getCurrentUser()
    private val utilisateurService : UtilisateurService = UtilisateurService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Récupération des éléments de la vue
        emailInput = view.findViewById(R.id.emailInput)
        nomInput = view.findViewById(R.id.usernameInput)
        btnMettreAJourNom = view.findViewById(R.id.modifyUsernameButton)
        btnMettreAJourEmail = view.findViewById(R.id.modifyEmailButton)

        //Recuperation des infos du user
        val ancienEmail = current_user?.email ?: "Email inconnu"
        val ancienNom = current_user?.nomComplet ?: "Nom inconnu"
        
        // Préremplit les EditText
        nomInput.setText(ancienNom)
        emailInput.setText(ancienEmail)

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

        return view
    }
}
