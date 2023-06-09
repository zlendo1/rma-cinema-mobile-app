package ba.etf.rma23.projekat.data.repositories.result


data class GameResult(
    val id: Int,
    val name: String,
    val platforms: List<Int>?,           // /platforms
    val first_release_date: Long?,        // in timestamp format
    val rating: Double?,
    val cover: Int?,                     // /covers
    val age_ratings: List<Int>?,         // /age_ratings
    val involved_companies: List<Int>?,  // /involved_companies -> companies
    val genres: List<Int>?,              // /genres
    val summary: String?
)
