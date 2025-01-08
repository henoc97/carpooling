package tg.ulcrsandroid.carpooling.domain.models

class Passager(
    userId: String,
    email: String,
    fullName: String,
    password: String,
    userType: String,
    var bookingHistory: List<Reservation>
) : Utilisateur(userId, email, fullName, password, userType)
