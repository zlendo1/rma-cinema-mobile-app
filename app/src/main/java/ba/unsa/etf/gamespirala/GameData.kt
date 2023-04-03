package ba.unsa.etf.gamespirala

import ba.unsa.etf.gamespirala.staticdata.Game

fun getAll(): List<Game> {
    return listOf()
}

fun getDetails(title: String): Game? {
    val games = getAll().filter { game -> game.title == title }

    return if (games.isEmpty())  null else games[0]
}