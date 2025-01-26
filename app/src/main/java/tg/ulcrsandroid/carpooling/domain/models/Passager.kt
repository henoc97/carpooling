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
    var historiqueReservations: List<Reservation>
) : Utilisateur(idUtilisateur, email, nomComplet, motDePasse, typeUtilisateur) {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Reservation.CREATOR) ?: emptyList()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeTypedList(historiqueReservations)
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
