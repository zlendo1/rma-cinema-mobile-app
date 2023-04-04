package ba.unsa.etf.gamespirala

import ba.unsa.etf.gamespirala.domain.Game

fun getAll(): List<Game> {
    return listOf()
}

fun getDetails(title: String): Game? {
    return getAll().find { game -> game.title == title }
}