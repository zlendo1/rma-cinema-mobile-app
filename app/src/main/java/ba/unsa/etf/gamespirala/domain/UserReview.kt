package ba.unsa.etf.gamespirala.domain

data class UserReview(
    override val username: String,
    override val timestamp: Long,
    val review: String
):UserImpression()
