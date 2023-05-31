package ba.etf.rma23.projekat.domain

data class UserRating(
    override val username: String,
    override val timestamp: Long,
    val rating: Double
):UserImpression()
