package tg.ulcrsandroid.carpooling.domain.models

import java.util.Date

class Chat(
    var chatId: String,
    var sender: Utilisateur,
    var receiver: Utilisateur,
    var message: String,
    var timestamp: Date
)
