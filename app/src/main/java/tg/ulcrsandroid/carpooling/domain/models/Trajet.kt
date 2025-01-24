package tg.ulcrsandroid.carpooling.domain.models

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import java.util.Date

@SuppressLint("ParcelCreator")
class Trajet(
    var idTrajet: String,
    var lieuDepart: String,
    var lieuArrivee: String,
    var heureDepart: Date,
    var prixParPassager: Float,
    var conducteur: Conducteur,
    var placesDisponibles: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Date(parcel.readLong()),
        parcel.readFloat(),
        parcel.readParcelable(Conducteur::class.java.classLoader)!!,
        parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(idTrajet)
        dest.writeString(lieuDepart)
        dest.writeString(lieuArrivee)
        dest.writeLong(heureDepart.time)
        dest.writeFloat(prixParPassager)
        dest.writeParcelable(conducteur, flags)
        dest.writeInt(placesDisponibles)
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
}
