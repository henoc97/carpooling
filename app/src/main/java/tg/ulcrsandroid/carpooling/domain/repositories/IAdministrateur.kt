package tg.ulcrsandroid.carpooling.domain.repositories

import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.domain.models.Trajet

interface IAdministrateur {
    fun gererUtilisateurs(onSuccess: (List<Utilisateur>) -> Unit, onError: (String) -> Unit)
    fun gererTrajets(onSuccess: (List<Trajet>) -> Unit, onError: (String) -> Unit)
    fun surveillerActivite(onSuccess: (List<String>) -> Unit, onError: (String) -> Unit)
}