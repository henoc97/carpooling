package tg.ulcrsandroid.carpooling.domain.models

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class Reservation(
    var idReservation: String,
    var passager: Passager,
    var trajet: Trajet,
    var heureReservation: Date,
    var statut: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readParcelable(Passager::class.java.classLoader)!!,
        parcel.readParcelable(Trajet::class.java.classLoader)!!,
        Date(parcel.readLong()),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(idReservation)
        dest.writeParcelable(passager, flags)
        dest.writeParcelable(trajet, flags)
        dest.writeLong(heureReservation.time)
        dest.writeString(statut)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Reservation> {
        override fun createFromParcel(parcel: Parcel): Reservation {
            return Reservation(parcel)
        }

        override fun newArray(size: Int): Array<Reservation?> {
            return arrayOfNulls(size)
        }
    }
}
