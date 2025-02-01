package tg.ulcrsandroid.carpooling.domain.models

class Chat(
    var idChat: String,
    var nomInitialisateur: String,
    var idInitialisateur: String,
    var nomMembreSecondaire: String,
    var idMembreSecondaire: String,
    var discussion: MutableList<Discussion> = mutableListOf()
) {
    constructor() : this("", "", "", "", "")

    override fun toString(): String {
        return "Chat(idChat='$idChat', nomInitialisateur='$nomInitialisateur', idInitialisateur='$idInitialisateur', nomMembreSecondaire='$nomMembreSecondaire', idMembreSecondaire='$idMembreSecondaire', discussion=$discussion)"
    }

}