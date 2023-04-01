package ba.unsa.etf.gamespirala.staticdata

data class UserReview(
    override val username: String,
    override val timestamp: Long,
    val review: String
):UserImpression()
