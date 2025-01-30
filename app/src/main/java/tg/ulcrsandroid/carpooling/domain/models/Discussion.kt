package tg.ulcrsandroid.carpooling.domain.models

import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import java.util.Date

class Discussion(
    var idDiscussion: String,
    var idExpediteur: String,
    var message: String,
    var horodatage: Date
) {

    constructor() : this("", "", "", Date())

    fun isSent(): Boolean {
        return if (idExpediteur == UserManager.getCurrentUser()?.idUtilisateur) true else false
    }

    override fun toString(): String {
        return "Discussion(idDiscussion='$idDiscussion', idExpediteur='$idExpediteur', message='$message', horodatage=$horodatage)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Discussion

        return idDiscussion == other.idDiscussion
    }

    override fun hashCode(): Int {
        return idDiscussion.hashCode()
    }


}