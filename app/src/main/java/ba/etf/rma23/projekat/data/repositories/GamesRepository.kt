package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.auxiliary.esrbToAge
import ba.etf.rma23.projekat.auxiliary.ratingToEsrb
import ba.etf.rma23.projekat.auxiliary.timestampToString
import ba.etf.rma23.projekat.data.repositories.result.*
import ba.etf.rma23.projekat.domain.Game
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
            val body = bodyBuilder("search \"$name\"")
            val response = IGDBApiConfig.retrofit.getGames(body)

            if (response.isSuccessful) {
                val result = response.body()!!

                games = resultToGame(result)

                return@withContext games
            }

            return@withContext emptyList<Game>()
        }
    }

    suspend fun getGamesSafe(name: String): List<Game> {
        return withContext(Dispatchers.IO) {
            val age = AccountRepository.account.age

            games = getGamesByName(name).filter { game -> age >= esrbToAge(game.esrbRating) }

            return@withContext games
        }
    }

    suspend fun sortGames(): List<Game> {
        return withContext(Dispatchers.IO) {
            val favoriteGames = AccountRepository.getSavedGames()

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
            MediaType.get("text/plain; chatset=utf-8"), "$content; fields *"
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
                val title = it.name
                val platform = getPlatformById(it.platforms.first())?.abbreviation!!
                val releaseDate = timestampToString(it.first_releaseDate)
                val rating = it.rating
                val coverImage = getCoverById(it.cover)?.url!!
                val genre = getGenreById(it.genres.first())?.name!!
                val description = it.summary

                val ageRatings: ArrayList<AgeRatingResult> = arrayListOf()

                for (it2 in it.age_ratings) {
                    ageRatings.add(getAgeRatingById(it2)!!)
                }

                val ageRatingResultNeeded = ageRatings.find { r -> r.rating == 1 || r.rating == 2 }

                val esrbRating = ratingToEsrb(ageRatingResultNeeded?.rating ?: 0)

                val involvedCompanies: ArrayList<InvolvedCompanyResult> = arrayListOf()

                for (it2 in it.involved_companies) {
                    involvedCompanies.add(getInvolvedCompanyById(it2)!!)
                }

                val developerResult = involvedCompanies.find { r -> r.developer }
                val publisherResult = involvedCompanies.find { r -> r.publisher }

                val developer = if (developerResult != null) getCompanyById(developerResult.company)?.name!! else ""
                val publisher = if (publisherResult != null) getCompanyById(publisherResult.company)?.name!! else ""

                games.add(
                    Game(id, title, platform, releaseDate, rating, coverImage, esrbRating, developer, publisher,genre, description, emptyList())
                )
            }

            return@withContext games
        }
    }

}