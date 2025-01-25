package tg.ulcrsandroid.carpooling.application.utils.authStrategies

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.application.utils.notification.FirebaseTokenManager

class EmailPasswordAuthStrategy : IAuthStrategy {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override fun sInscrire(
        email: String?,
        password: String?,
        nomComplet: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email == null || password == null || nomComplet == null) {
            onError("Email, mot de passe ou nom complet manquant.") // Appeler onError si un champ est manquant
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idUtilisateur = task.result?.user?.uid
                    if (idUtilisateur != null) {
                        val user = mapOf(
                            "idUtilisateur" to idUtilisateur,
                            "email" to email,
                            "nomComplet" to nomComplet
                        )
                        database.child("users").child(idUtilisateur).setValue(user)
                            .addOnSuccessListener {
                                FirebaseTokenManager.updateToken(idUtilisateur) // Mise à jour du token
                                onSuccess() // Appeler onSuccess si tout est réussi
                            }
                            .addOnFailureListener { e ->
                                onError("Erreur d'enregistrement : ${e.message}") // Appeler onError en cas d'échec
                            }
                    } else {
                        onError("Erreur : ID utilisateur non trouvé après inscription.")
                    }
                } else {
                    onError("Erreur d'inscription : ${task.exception?.message}") // Appeler onError en cas d'échec
                }
            }
    }

    override fun seConnecter(
        email: String?,
        password: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email == null || password == null) {
            onError("Email ou mot de passe manquant.") // Appeler onError si un champ est manquant
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        val idUtilisateur = user.uid // Récupérer l'ID de l'utilisateur
                        FirebaseTokenManager.updateToken(idUtilisateur) // Mettre à jour le token
                        onSuccess() // Appeler onSuccess si tout est réussi
                    } else {
                        onError("Erreur : Utilisateur non trouvé après connexion.") // Appeler onError en cas d'échec
                    }
                } else {
                    onError("Erreur de connexion : ${task.exception?.message}") // Appeler onError en cas d'échec
                }
            }
    }
}