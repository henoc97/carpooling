package tg.ulcrsandroid.carpooling.domain.models

class Chat(
    val idChat: String,
    val nomInitialisateur: String,
    val idInitialisateur: String,
    val nomMembreSecondaire: String,
    val idMembreSecondaire: String,
    var discussion: MutableList<Discussion> = mutableListOf()
) {
}