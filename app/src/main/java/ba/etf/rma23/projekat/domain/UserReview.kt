package ba.etf.rma23.projekat.domain

data class UserReview(
    override val username: String,
    override val timestamp: Long,
    val review: String
):UserImpression()
