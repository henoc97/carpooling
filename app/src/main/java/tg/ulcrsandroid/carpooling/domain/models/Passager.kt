package tg.ulcrsandroid.carpooling.domain.models

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
class Passager(
    idUtilisateur: String,
    email: String,
    nomComplet: String,
    motDePasse: String,
    typeUtilisateur: String,
    var historiqueReservations: List<Reservation>,
    var reservationsIds: MutableList<String>? = null
) : Utilisateur(idUtilisateur, email, nomComplet, motDePasse, typeUtilisateur) {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Reservation.CREATOR) ?: emptyList(),
        parcel.createStringArrayList() ?: mutableListOf()
    )

    constructor() : this(
         "",
         "",
         "",
         "",
         "",
         emptyList(),
         mutableListOf()
    )

    constructor(utilisateur: Utilisateur) : this(
        utilisateur.idUtilisateur,
        utilisateur.email,
        utilisateur.nomComplet,
        utilisateur.motDePasse,
        utilisateur.typeUtilisateur,
        emptyList(),
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeTypedList(historiqueReservations)
    }

    override fun toString(): String {
        return "Passager(historiqueReservations=$historiqueReservations, reservationsIds=$reservationsIds)"
    }

    companion object CREATOR : Parcelable.Creator<Passager> {
        override fun createFromParcel(parcel: Parcel): Passager {
            return Passager(parcel)
        }

        override fun newArray(size: Int): Array<Passager?> {
            return arrayOfNulls(size)
        }
    }


}
