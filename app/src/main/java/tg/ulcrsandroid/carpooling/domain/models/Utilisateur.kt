package tg.ulcrsandroid.carpooling.domain.models

import tg.ulcrsandroid.carpooling.application.services.UtilisateurService

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
open class Utilisateur(
    var idUtilisateur: String,
    var email: String,
    var nomComplet: String,
    var motDePasse: String,
    var typeUtilisateur: String,
) : Parcelable {

    var mesChats: MutableList<String> = mutableListOf()
    constructor() : this("", "", "", "", "")

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(idUtilisateur)
        dest.writeString(email)
        dest.writeString(nomComplet)
        dest.writeString(motDePasse)
        dest.writeString(typeUtilisateur)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Utilisateur> {

        override fun createFromParcel(parcel: Parcel): Utilisateur {
            return Utilisateur(parcel)
        }
        override fun newArray(size: Int): Array<Utilisateur?> {
            return arrayOfNulls(size)
        }
    }

    fun ajouterIdDiscussion(idDiscussion: String) {
        mesChats.add(idDiscussion)
        UtilisateurService.mettreAJourProfil(this) // Mettre à jour le profil de l'utilisateur
    }

    override fun toString(): String {
        return "Utilisateur(idUtilisateur='$idUtilisateur', email='$email', nomComplet='$nomComplet', motDePasse='$motDePasse', typeUtilisateur='$typeUtilisateur', mesChats=$mesChats)"
    }


}
