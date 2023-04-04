package ba.unsa.etf.gamespirala.domain

data class UserRating(
    override val username: String,
    override val timestamp: Long,
    val rating: Double
):UserImpression()
