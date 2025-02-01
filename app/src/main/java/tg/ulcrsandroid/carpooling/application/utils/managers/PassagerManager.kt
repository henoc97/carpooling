package tg.ulcrsandroid.carpooling.application.utils.managers

import tg.ulcrsandroid.carpooling.application.services.PassagerService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.domain.models.Passager
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur

object PassagerManager {

    private var passagerActuel: Passager? = null

    fun setPassagerActuel(passager: Passager?) {
        passagerActuel = passager
    }

    fun getPassagerActuel(): Passager? {
//        if (passagerActuel == null) {
//            passagerActuel = PassagerService.recupererPassager(UserManager.getCurrentUser()!!.idUtilisateur)
//        }
        return passagerActuel
    }

    fun mettreAjourPassager() {
        PassagerService.persisterPassager(passagerActuel!!)
    }
}