package tg.ulcrsandroid.carpooling.domain.models

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class Trajet(
    var idTrajet: String = "",
    var lieuDepart: String = "",
    var lieuArrivee: String = "",
    var heureDepart: Long = 0, // Timestamp (millisecondes)
    var prixParPassager: Float = 0f,
    var placesDisponibles: Int = 0,
    var conducteur: Conducteur? = null,
    var idConducteur: String = "",
    var creeA: Long = 0, // Timestamp (millisecondes)
    var reservations: MutableList<Reservation>? = mutableListOf(),
    var reservationsIds: MutableList<String> = mutableListOf()
) : Parcelable {

    // Constructeur sans arguments (nécessaire pour Firebase)
    constructor() : this("", "", "", 0, 0f, 0, null, "", 0)

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readParcelable(Conducteur::class.java.classLoader),
        parcel.readString() ?: "",
        parcel.readLong()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(idTrajet)
        dest.writeString(lieuDepart)
        dest.writeString(lieuArrivee)
        dest.writeLong(heureDepart)
        dest.writeFloat(prixParPassager)
        dest.writeInt(placesDisponibles)
        dest.writeParcelable(conducteur, flags)
        dest.writeString(idConducteur)
        dest.writeLong(creeA)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Trajet> {
        override fun createFromParcel(parcel: Parcel): Trajet {
            return Trajet(parcel)
        }

        override fun newArray(size: Int): Array<Trajet?> {
            return arrayOfNulls(size)
        }
    }

    // Méthode utilitaire pour obtenir une Date à partir du timestamp
    fun getHeureDepartDate(): Date {
        return Date(heureDepart)
    }

    fun getCreeADate(): Date {
        return Date(creeA)
    }

    override fun toString(): String {
        return "Trajet(idTrajet='$idTrajet', lieuDepart='$lieuDepart', lieuArrivee='$lieuArrivee', heureDepart=$heureDepart, prixParPassager=$prixParPassager, placesDisponibles=$placesDisponibles, conducteur=$conducteur, idConducteur='$idConducteur', creeA=$creeA)"
    }


}