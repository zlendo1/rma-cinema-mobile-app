package ba.etf.rma23.projekat.auxiliary

import ba.etf.rma23.projekat.domain.Account
import ba.etf.rma23.projekat.domain.Game

fun ratingToEsrb(rating: Int): String {
    return when (rating) {
        1 -> "Three"
        2 -> "Seven"
        3 -> "Twelve"
        4 -> "Sixteen"
        5 -> "Eighteen"
        7 -> "EC"
        8 -> "E"
        9 -> "E10"
        10 -> "T"
        11 -> "M"
        12 -> "AO"
        else -> ""
    }
}

fun esrbToAge(esrbRating: String): Int {
    return when (esrbRating) {
        "Three" -> 3
        "Seven" -> 7
        "Twelve" -> 12
        "Sixteen" -> 16
        "Eighteen" -> 18
        "EC" -> 3
        "E" -> 6
        "E10" -> 10
        "T" -> 13
        "M" -> 17
        "AO" -> 18
        else -> 0
    }
}

fun timestampToString(timestamp: Int): String {
    return java.time.format.DateTimeFormatter.ISO_INSTANT
        .format(java.time.Instant.ofEpochSecond(timestamp.toLong()))
}

fun isAgeSafe(account: Account, game: Game): Boolean {
    return account.age >= esrbToAge(game.esrbRating)
}