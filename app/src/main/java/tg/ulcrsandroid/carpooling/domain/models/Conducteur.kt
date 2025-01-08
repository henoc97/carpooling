package tg.ulcrsandroid.carpooling.domain.models

class Conducteur(
    userId: String,
    email: String,
    fullName: String,
    password: String,
    userType: String,
    var vehicleDetails: String,
    var availableSeats: Int
) : Utilisateur(userId, email, fullName, password, userType)
