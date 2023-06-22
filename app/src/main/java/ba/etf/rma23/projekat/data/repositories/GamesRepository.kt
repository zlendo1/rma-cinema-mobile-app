package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.auxiliary.isAgeSafe
import ba.etf.rma23.projekat.auxiliary.ratingToEsrb
import ba.etf.rma23.projekat.auxiliary.timestampToString
import ba.etf.rma23.projekat.data.repositories.result.*
import ba.etf.rma23.projekat.domain.Game
import ba.etf.rma23.projekat.domain.UserImpression
import ba.etf.rma23.projekat.domain.UserRating
import ba.etf.rma23.projekat.domain.UserReview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import kotlin.reflect.KSuspendFunction1

object GamesRepository {

    private var games: List<Game> = emptyList()

    suspend fun getGamesByName(name: String): List<Game> {
        return withContext(Dispatchers.IO) {
            val body = bodyBuilder("search \"$name\"; limit 20")
            val response = IGDBApiConfig.retrofit.getGames(body)

            if (response.isSuccessful) {
                val result = response.body()!!

                games = resultToGame(result)

                return@withContext games
            }

            return@withContext emptyList()
        }
    }

    suspend fun getGamesSafe(name: String): List<Game> {
        return withContext(Dispatchers.IO) {
            games = getGamesByName(name)
                .filter { game -> isAgeSafe(AccountGamesRepository.account, game) }

            return@withContext games
        }
    }

    suspend fun sortGames(): List<Game> {
        return withContext(Dispatchers.IO) {
            val favoriteGames = AccountGamesRepository.getSavedGames()

            val predicate = { gameFirst:Game ->
                favoriteGames.find { gameSecond ->
                    gameFirst.id == gameSecond.id
                } != null
            }

            val firstPart = games.filter(predicate).sorted()
            val secondPart = games.filterNot(predicate).sorted()

            games = firstPart + secondPart

            return@withContext games
        }
    }

    private fun bodyBuilder(content: String): RequestBody
        = RequestBody.create(
            MediaType.get("text/plain; chatset=utf-8"), "$content; fields *;"
        )

    suspend fun getGameById(id: Int): GameResult?
        = getResultById(id, IGDBApiConfig.retrofit::getGames)

    private suspend fun getPlatformById(id: Int): PlatformResult?
        = getResultById(id, IGDBApiConfig.retrofit::getPlatforms)

    private suspend fun getCoverById(id: Int): CoverResult?
        = getResultById(id, IGDBApiConfig.retrofit::getCovers)

    private suspend fun getAgeRatingById(id: Int): AgeRatingResult?
        = getResultById(id, IGDBApiConfig.retrofit::getAgeRatings)

    private suspend fun getInvolvedCompanyById(id: Int): InvolvedCompanyResult?
        = getResultById(id, IGDBApiConfig.retrofit::getInvolvedCompanies)

    private suspend fun getCompanyById(id: Int): CompanyResult?
        = getResultById(id, IGDBApiConfig.retrofit::getCompanies)

    private suspend fun getGenreById(id: Int): GenreResult?
        = getResultById(id, IGDBApiConfig.retrofit::getGenres)

    private suspend fun <T> getResultById(id: Int, retrofitCall: KSuspendFunction1<RequestBody, Response<List<T>>>): T? {
        return withContext(Dispatchers.IO) {
            val body = bodyBuilder("where id = $id")
            val response = retrofitCall(body)

            if (response.isSuccessful) {
                val result = response.body()

                if (!result.isNullOrEmpty()) {
                    return@withContext result.first()
                }
            }

            return@withContext null
        }
    }

    suspend fun resultToGame(gameResults: List<GameResult>): List<Game> {
        return withContext(Dispatchers.IO) {
            val games: ArrayList<Game> = arrayListOf()

            for (it in gameResults) {
                val id = it.id
                val title: String = it.name
                val platform: String = it.platforms?.let { it1 -> getPlatformById(it1.first())?.abbreviation } ?: ""
                val releaseDate: String = it.first_release_date?.let { it1 -> timestampToString(it1) } ?: ""
                val rating: Double = it.rating ?: 0.0
                val coverImage: String = it.cover?.let { it1 -> getCoverById(it1)?.url } ?: ""
                val genre: String = it.genres?.let { it1 -> getGenreById(it1.first())?.name } ?: ""
                val description: String = it.summary ?: ""

                val ageRatings: ArrayList<AgeRatingResult> = arrayListOf()

                it.age_ratings?.forEach { it1 ->
                    ageRatings.add(getAgeRatingById(it1)!!)
                }

                val ageRatingResultNeeded = ageRatings.find { r -> r.category in 1..2 }

                val esrbRating = ratingToEsrb(ageRatingResultNeeded?.rating ?: 0)

                val involvedCompanies: ArrayList<InvolvedCompanyResult> = arrayListOf()

                it.involved_companies?.forEach { it1 ->
                    involvedCompanies.add(getInvolvedCompanyById(it1)!!)
                }

                val developerResult = involvedCompanies.find { r -> r.developer }
                val publisherResult = involvedCompanies.find { r -> r.publisher }

                val developer = if (developerResult != null) getCompanyById(developerResult.company)?.name!! else ""
                val publisher = if (publisherResult != null) getCompanyById(publisherResult.company)?.name!! else ""

                val gameReviews = GameReviewsRepository.getReviewsForGame(it.id)
                val userImpressions: ArrayList<UserImpression> = arrayListOf()

                gameReviews.forEach {
                    val timestamp = it.timestamp?.toLong() ?: 0
                    val username = it.student ?: ""

                    if (it.rating != null) {
                        userImpressions.add(UserRating(username, timestamp, it.rating.toDouble()))
                    }

                    if (it.review != null) {
                        userImpressions.add(UserReview(username, timestamp, it.review))
                    }
                }

                games.add(
                    Game(id, title, platform, releaseDate, rating, coverImage, esrbRating, developer, publisher,genre, description, userImpressions)
                )
            }

            return@withContext games
        }
    }

}